import com.piro.bezier.BezierPath;

import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.Cylinder
import eu.mihosoft.vrl.v3d.Extrude
import eu.mihosoft.vrl.v3d.Vector3d
import eu.mihosoft.vrl.v3d.parametrics.LengthParameter
CSGDatabase.clear()
double mm(double input){
	return input*25.4
}
ArrayList<CSG> make(){
	double boningWidth =8
	double seamInset = boningWidth*2+2


	// horizontal

	//bustSize 		= new LengthParameter("Bust Size",30,[120.0,1.0])
	LengthParameter underbust		= new LengthParameter ("underbust",mm(34),[120.0, 1.0])
	LengthParameter waist 		= new LengthParameter("waist",mm(30),[120.0, 1.0])
	LengthParameter highHip		= new LengthParameter("high hip",mm(39),[120.0, 1.0])
	LengthParameter lowHip 		= new LengthParameter("low hip",mm(41),[120.0, 1.0])
	// verticals
	//upBreast 		= new LengthParameter("up breast",30,[120.0,1.0])
	//downbreast	= new LengthParameter("down breast",30,[120.0,1.0])
	//midBreast		= new LengthParameter("middle of breast",30,[120.0,1.0])
	LengthParameter uBreastToWaist	= new LengthParameter("under breast to waist",110,[120.0, 1.0])
	LengthParameter waistToPubic 	= new LengthParameter("waist to pubic bone",210,[120.0, 1.0])
	LengthParameter waistHighHip	= new LengthParameter("waist high hip",mm(4),[120.0, 1.0])
	//waistLowHip	= new LengthParameter("waist low hip",30,[120.0,1.0])
	LengthParameter waistBackTop	= new LengthParameter("waist to top back",mm(7),[120.0, 1.0])
	LengthParameter waistBackBottom= new LengthParameter("waist to bottom back",mm(6.75),[120.0, 1.0])
	// construction
	LengthParameter numPanels	= new LengthParameter("number of panels",8,[12, 4])

	LengthParameter numRowsRivetsParam = new LengthParameter("number rows or rivets",1,[1, 2])
	LengthParameter buskLength = new LengthParameter("Busk Length",300,[100,600])
	LengthParameter buskLatchWidth = new LengthParameter("Busk Latch Width",13.5,[10,20])
	LengthParameter buskLatchFromEdge = new LengthParameter("Busk Latch From Edge",15.5,[10,20])
	LengthParameter buskLatchCenterToCenter = new LengthParameter("Busk Center To Center",52,[10,60])
	LengthParameter buskNubinFromEdge = new LengthParameter("Busk Nubon from Edge",4.5,[1,8])

	
	ArrayList<CSG> panels=[]
	int panelsPerSide = numPanels.getMM()/2


	for(int i=0;i<panelsPerSide;i++){
		double seamAllowance = ((seamInset+boningWidth)*(panelsPerSide-1))/panelsPerSide
		double panelMaxWidth = lowHip.getMM()/numPanels.getMM()+seamAllowance
		double waistSecion = waist.getMM()/numPanels.getMM()+seamAllowance
		double widthDifference = (panelMaxWidth-waistSecion)/2
		widthDifference=widthDifference+(widthDifference/numPanels.getMM())
		double MinHeightUpper = uBreastToWaist.getMM()
		double MaxHeightUpper =waistBackTop.getMM()
		double MinHeightLower = waistHighHip.getMM()
		double MaxHeightLower =waistBackBottom.getMM()

		double incrementA = Math.sin(Math.PI*(i)/panelsPerSide)
		double incrementB = Math.sin(Math.PI*(i+1)/panelsPerSide)
		if(i>=(panelsPerSide/2)){
			MinHeightUpper = uBreastToWaist.getMM()
			MaxHeightUpper =uBreastToWaist.getMM()
			MinHeightLower = waistHighHip.getMM()
			MaxHeightLower =waistToPubic.getMM()

		}

		//println "Increment A = "+incrementA+" increment b "+incrementB
		double heightDifferenceUpper =MaxHeightUpper- MinHeightUpper
		double heightDifferenceLower =MaxHeightLower- MinHeightLower

		double heightRightUpper = -(MaxHeightUpper-(heightDifferenceUpper*incrementA))
		double heightLeftUpper = -(MaxHeightUpper-(heightDifferenceUpper*incrementB))
		double heightRightLower = MaxHeightLower-(heightDifferenceLower*incrementA)
		double heightLeftLower = MaxHeightLower-(heightDifferenceLower*incrementB)

		double controlOffsetRight = heightRightLower/4
		double controlOffsetLeft  = heightLeftLower/4
		double upperWidth = underbust.getMM()/numPanels.getMM()+seamAllowance
		double upperDiff = (panelMaxWidth-upperWidth)/2

		Vector3d upperRight = 		new Vector3d(upperDiff,heightRightUpper,0)
		Vector3d centerRight =		new Vector3d(widthDifference,0,0)
		Vector3d bottomRight = 		new Vector3d(0,heightRightLower,0)
		Vector3d bottomLeft  = 		new Vector3d(panelMaxWidth,heightLeftLower,0)
		Vector3d centerLeft = 		new Vector3d(panelMaxWidth-widthDifference,0,0)
		Vector3d upperleft = 		new Vector3d(panelMaxWidth-upperDiff,heightLeftUpper,0)

		List<Vector3d> rightSideLower=[
			centerRight,
			new Vector3d(widthDifference,controlOffsetRight,0),
			new Vector3d(0,heightRightLower-controlOffsetRight ,0),
			bottomRight
		]
		List<Vector3d> leftSideLower =[
			bottomLeft,
			new Vector3d(panelMaxWidth,heightLeftLower-controlOffsetLeft,0),
			new Vector3d(panelMaxWidth-widthDifference,controlOffsetLeft,0),
			centerLeft
		]
		List<Vector3d> bottom =[
			bottomRight,
			new Vector3d(widthDifference,heightRightLower,0),
			new Vector3d(panelMaxWidth-widthDifference,heightLeftLower,0),
			bottomLeft
		]
		List<Vector3d> rightSideUpper=[
			upperRight,
			new Vector3d(upperDiff,heightRightUpper+controlOffsetRight,0),
			new Vector3d(widthDifference,-controlOffsetRight ,0),
			centerRight
		]
		List<Vector3d> leftSideUpper =[
			centerLeft,
			new Vector3d(panelMaxWidth-widthDifference,-controlOffsetLeft,0),
			new Vector3d(panelMaxWidth-upperDiff,heightLeftUpper+controlOffsetLeft,0),
			upperleft
		]
		List<Vector3d> top =[
			upperleft,
			new Vector3d(panelMaxWidth-widthDifference,heightLeftUpper,0),
			new Vector3d(widthDifference,heightRightUpper,0),
			upperRight
		]
		if(i==(panelsPerSide-1)||
		i==numPanels.getMM()-1){
			leftSideLower =[
				new Vector3d(panelMaxWidth,heightLeftLower,0),
				new Vector3d(panelMaxWidth-(upperDiff/8),heightLeftLower*3/4,0),
				new Vector3d(panelMaxWidth-(upperDiff/4),heightLeftLower/4,0),
				new Vector3d(panelMaxWidth-(upperDiff/2),0,0)
			]
			leftSideUpper =[
				new Vector3d(panelMaxWidth-(upperDiff/2),0,0),
				new Vector3d(panelMaxWidth-(upperDiff/1.5),heightLeftUpper/4,0),
				new Vector3d(panelMaxWidth-(upperDiff/1.25),heightRightUpper*3/4,0),
				new Vector3d(panelMaxWidth-(upperDiff/1),heightLeftUpper,0)
			]
		}
		if(i==(panelsPerSide)||i==0){
			rightSideUpper=[
				new Vector3d(upperDiff,heightRightUpper,0),
				new Vector3d(upperDiff/1.25,heightRightUpper*3/4,0),
				new Vector3d(upperDiff/1.5,heightRightUpper/4,0),
				new Vector3d(upperDiff/2,0,0)
			]
			rightSideLower=[
				new Vector3d(upperDiff/2,0,0),
				new Vector3d(upperDiff/4,heightRightLower/4,0),
				new Vector3d(upperDiff/8,heightRightLower*3/4,0),
				new Vector3d(0,heightRightLower,0)
			]
		}

		List<List<Vector3d>>  profile = [
			rightSideLower,
			bottom,
			leftSideLower,
			leftSideUpper,
			top,
			rightSideUpper
		]
		
		//println profile
		CSG shape = Extrude.byPath(profile,5)
		//CSG shape = new Cube(2,2,30).toCSG()
		def rivetDiam=3.18
		CSG hole = new Cube(rivetDiam,rivetDiam,30).toCSG()
		int NumRowsOfRivets =numRowsRivetsParam.getMM()

		hole = hole.toYMax()//.toXMax()
				.movey(NumRowsOfRivets==2?
				-seamInset/2-boningWidth/2
				:-seamInset/2)


		if(NumRowsOfRivets==2)
			hole=hole.union(hole.toYMin()//.toXMin()
					.movey((-seamInset/2)+boningWidth/2)
					)

		CSG holeR =  hole
				.movex(mm(0.3))

		CSG holeL =  hole
				.movex(mm(-0.3))
		
		// Lacing interface		
		if(i==0){
			def ydiff=Math.abs(-heightRightUpper+heightRightLower)
			def xdiff=upperDiff
			def angle = Math.toDegrees(Math.atan2(xdiff, ydiff))
			//println "Ydiff:"+ ydiff+" xdiff="+xdiff+" angle "+angle
			
			def laceFromEdge=14
			def laceHole=new Cylinder(7.14/2,30,(int)10).toCSG()
								.movez(-15)
			holeR = laceHole
					.movey(-laceFromEdge)
			.union(hole.movey(-laceFromEdge-seamInset/2))
			.union(hole.movey(laceFromEdge*2+seamInset-rivetDiam/2))
			.union(laceHole.movey(laceFromEdge))
			CSG fold = new Cube(laceFromEdge*2+seamInset,ydiff,5).toCSG()
						.toXMax()
						.toYMax()
						.toZMin()
						.rotz(-angle)
						.movey(heightRightLower)
						.movex(0.2)
			shape=shape.union(fold)
		}
		double centerDistanceOfLatch = mm(1.0+1.0/8.0)
		double spacing = (i*panelMaxWidth)//+ (10*i)
		boolean isBusk=(i==(panelsPerSide-1))
		// Busk section
		if(isBusk){
			def buskLengthV=buskLength.getMM()
			def buskLatchWidthV=buskLatchWidth.getMM()
			def buskLatchFromEdgeV=buskLatchFromEdge.getMM()
			def buskLatchCenterToCenterV=buskLatchCenterToCenter.getMM()
			def buskNubinFromEdgeV=buskNubinFromEdge.getMM()
			
			def ydiff=Math.abs(-heightLeftUpper+heightLeftLower)
			def xdiff=upperDiff
			def foldLength=Math.sqrt(Math.pow(xdiff, 2)+Math.pow(ydiff, 2))
			def angle = Math.toDegrees(Math.atan2(xdiff, ydiff))
			println "Ydiff:"+ ydiff+" xdiff="+xdiff+" angle "+angle
			def buskCentering=(ydiff-buskLengthV)/2
			
			CSG buskCore = new Cube(buskLatchWidthV,buskLengthV,1).toCSG()
			CSG buskTabs=buskCore
			CSG buskNubs=buskCore
			int numBuskTabs =buskLengthV/buskLatchCenterToCenterV+1
			println "Num busk tabs: "+numBuskTabs
			CSG tab = new Cube(1,buskLatchWidthV,7).toCSG()
						.toZMax()
						.movex(buskLatchWidthV/2)
			CSG nub = new Cube(rivetDiam,rivetDiam,7).toCSG()
						.toZMax()
						.movex(buskLatchWidthV/2 - buskNubinFromEdgeV)
			for(double j=0;j<numBuskTabs;j+=1) {
				double loc = buskLatchFromEdgeV+(buskLatchCenterToCenterV*j)-buskLengthV/2
				buskTabs=buskTabs.union(tab.movey(loc))
				buskNubs=buskNubs.union(nub.movey(loc))
			}
								
			def busksBits=[buskNubs,buskTabs]

			//shape=shape.difference(buskCore)
			panels.addAll(busksBits.collect{
						def moved=((CSG)it)
						.movez(6)
						.toYMin()
						.toXMax()
						.movey(buskCentering)
						.rotz(angle)
						.movey(heightLeftUpper)
						.movex(panelMaxWidth-upperDiff)
						.movex(spacing)
						moved.addExportFormat("svg")
						return moved
			})
						
						
			def locationOfFoldedRivets = seamInset/2+buskLatchWidthV+rivetDiam*2
			holeL= hole
				.movex(mm(-0.3))
				.movey(-buskLatchWidthV+rivetDiam)
			holeL=holeL.union(hole
				.movex(mm(-0.3))
				.movey(locationOfFoldedRivets))
			
			CSG fold = new Cube(locationOfFoldedRivets,ydiff,5).toCSG()
						.toYMin()
						.toXMin()
						.toZMin()
						.rotz(angle)
						.movey(heightLeftUpper)
						.movex(panelMaxWidth-upperDiff)
			shape=shape.union(fold)

		}
		int holesPerSide = 7
		//println "Loading from lib"
		def llower  =Extrude.bezierToTransforms((List<Vector3d> )leftSideLower, i==(panelsPerSide-1)?5: holesPerSide)
		def rlower  =Extrude.bezierToTransforms((List<Vector3d> )rightSideLower,  holesPerSide)
		def rUpper = Extrude.bezierToTransforms((List<Vector3d> )rightSideUpper,  holesPerSide)
		def lUpper  =Extrude.bezierToTransforms((List<Vector3d> )leftSideUpper, i==(panelsPerSide-1)?4: holesPerSide)
		rlower.remove(0)
		lUpper.remove(0)
		llower.remove(0)
		//rUpper.remove(0)
		def llHole = Extrude.move(holeL,llower).collect{it.movex(spacing)}
		def lrHole = Extrude.move(holeR,rlower).collect{it.movex(spacing)}
		def ulHole = Extrude.move(holeL,lUpper).collect{it.movex(spacing)}
		def urHole = Extrude.move(holeR,rUpper).collect{it.movex(spacing)}
		shape=shape.movex(spacing)

		CSG ShapeWithHoles=shape
				.difference( urHole)
				.difference( lrHole)
		//if(!isBusk)
			ShapeWithHoles=ShapeWithHoles
				.difference( llHole)
				.difference( ulHole)
		//if(i==(panelsPerSide-1))
		//	shape=shape .movex((-panelMaxWidth)- (10))
		//else
		ShapeWithHoles.addExportFormat("svg")
		ShapeWithHoles.setName(i+" panel withHoles")
		ShapeWithHoles.setParameter(underbust	)
		ShapeWithHoles.setParameter(waist 		)
		ShapeWithHoles.setParameter(highHip		)
		ShapeWithHoles.setParameter(lowHip 		)
		ShapeWithHoles.setParameter(uBreastToWaist)
		ShapeWithHoles.setParameter(waistToPubic )
		ShapeWithHoles.setParameter(waistHighHip)
		ShapeWithHoles.setParameter(waistBackTop)
		ShapeWithHoles.setParameter(waistBackBottom)
		ShapeWithHoles.setParameter(numPanels	)
		ShapeWithHoles.setParameter(numRowsRivetsParam )
		ShapeWithHoles.setParameter(buskLength)
		ShapeWithHoles.setParameter(buskLatchWidth)
		ShapeWithHoles.setParameter(buskLatchFromEdge)
		ShapeWithHoles.setParameter(buskLatchCenterToCenter)
		ShapeWithHoles.setParameter(buskNubinFromEdge)
		
		int arrayIndex=i
		ShapeWithHoles.setRegenerate({ (CSG)make().get(arrayIndex)})// add a regeneration function to the CSG being returrned to lonk a change event to a re-render

		shape=shape.movez(-2.5)
		shape.addExportFormat("svg")
		shape.setName(i+" panel")
		panels.addAll([ShapeWithHoles])

		//if(i==0){

		//Image ruler = AssetFactory.loadAsset("BowlerStudio-Icon.png");
		//ImageView rulerImage = new ImageView(ruler);
		//Slice.slice(shape,slicePlane, 0)
		println "Panel width "+ ((shape.getTotalX()-seamInset)/25.4)*panelsPerSide*2

		//}

	}
	return panels
}

return make()
