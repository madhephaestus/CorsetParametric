import com.piro.bezier.BezierPath;
CSGDatabase.clear()
double mm(double input){
	return input*25.4
}
double boningWidth =8
double seamInset = boningWidth*2+2


// horizontal

//bustSize 		= new LengthParameter("Bust Size",30,[120.0,1.0])
underbust		= new LengthParameter("underbust",mm(34),[120.0,1.0])
waist 		= new LengthParameter("waist",mm(30),[120.0,1.0])
highHip		= new LengthParameter("high hip",mm(39),[120.0,1.0])
lowHip 		= new LengthParameter("low hip",mm(41),[120.0,1.0])
// verticals
//upBreast 		= new LengthParameter("up breast",30,[120.0,1.0])
//downbreast	= new LengthParameter("down breast",30,[120.0,1.0])
//midBreast		= new LengthParameter("middle of breast",30,[120.0,1.0])
uBreastToWaist	= new LengthParameter("under breast to waist",mm(4),[120.0,1.0])
waistToPubic 	= new LengthParameter("waist to pubic bone",mm(6),[120.0,1.0])
waistHighHip	= new LengthParameter("waist high hip",mm(4),[120.0,1.0])
//waistLowHip	= new LengthParameter("waist low hip",30,[120.0,1.0])
waistBackTop	= new LengthParameter("waist to top back",mm(7),[120.0,1.0])
waistBackBottom= new LengthParameter("waist to bottom back",mm(6.75),[120.0,1.0])
// construction
numPanels	= new LengthParameter("number of panels",8,[12,4])


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
	
	List<Vector3d> rightSideLower=[	centerRight,
							new Vector3d(widthDifference,controlOffsetRight,0),
							new Vector3d(0,heightRightLower-controlOffsetRight ,0),
							bottomRight]
	List<Vector3d> leftSideLower =[bottomLeft,
							new Vector3d(panelMaxWidth,heightLeftLower-controlOffsetLeft,0),
							new Vector3d(panelMaxWidth-widthDifference,controlOffsetLeft,0),
							centerLeft]
	List<Vector3d> bottom =[ bottomRight,
							new Vector3d(widthDifference,heightRightLower,0),
							new Vector3d(panelMaxWidth-widthDifference,heightLeftLower,0),
							bottomLeft]
	List<Vector3d> rightSideUpper=[	upperRight,
							new Vector3d(upperDiff,heightRightUpper+controlOffsetRight,0),
							new Vector3d(widthDifference,-controlOffsetRight ,0),
							centerRight]
	List<Vector3d> leftSideUpper =[centerLeft,
							new Vector3d(panelMaxWidth-widthDifference,-controlOffsetLeft,0),
							new Vector3d(panelMaxWidth-upperDiff,heightLeftUpper+controlOffsetLeft,0),
							upperleft]
	List<Vector3d> top =[	upperleft,
							new Vector3d(panelMaxWidth-widthDifference,heightLeftUpper,0),
							new Vector3d(widthDifference,heightRightUpper,0),
							upperRight]	
	if(i==(panelsPerSide-1)||
	   i==numPanels.getMM()-1){
		leftSideLower =[new Vector3d(panelMaxWidth,heightLeftLower,0),
				new Vector3d(panelMaxWidth-(upperDiff/8),heightLeftLower*3/4,0),
				new Vector3d(panelMaxWidth-(upperDiff/4),heightLeftLower/4,0),
				new Vector3d(panelMaxWidth-(upperDiff/2),0,0)]
		leftSideUpper =[	new Vector3d(panelMaxWidth-(upperDiff/2),0,0),			
						new Vector3d(panelMaxWidth-(upperDiff/1.5),heightLeftUpper/4,0),
						new Vector3d(panelMaxWidth-(upperDiff/1.25),heightRightUpper*3/4,0),
						new Vector3d(panelMaxWidth-(upperDiff/1),heightLeftUpper,0)]
	}
	if(i==(panelsPerSide)||i==0){
		rightSideUpper=[	new Vector3d(upperDiff,heightRightUpper,0),
				new Vector3d(upperDiff/1.25,heightRightUpper*3/4,0),
				new Vector3d(upperDiff/1.5,heightRightUpper/4,0),
				new Vector3d(upperDiff/2,0,0)]
		rightSideLower=[	new Vector3d(upperDiff/2,0,0),
				new Vector3d(upperDiff/4,heightRightLower/4,0),
				new Vector3d(upperDiff/8,heightRightLower*3/4,0),
				new Vector3d(0,heightRightLower,0)]
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
	CSG hole = new Cube(3.18,3.18,30).toCSG()
	hole = hole.toYMax()//.toXMax()
			.movey(-seamInset/2-boningWidth/2)
			.union(hole.toYMin()//.toXMin()
			.movey((-seamInset/2)+boningWidth/2)
			)
	int NumRowsOfRivets =1				
	CSG holeR =  hole
				.movex(NumRowsOfRivets==2?mm(0.3):0)
					
	CSG holeL =  hole
				.movex(NumRowsOfRivets==2?mm(-0.3):0)
					
	if(i==0){
		holeR =  new Cylinder(7.14/2,30,(int)10).toCSG()
					.movey(-7)					
					.movez(-15)
	}
	double centerDistanceOfLatch = mm(1.0+1.0/8.0)
	if(i==(panelsPerSide-1)){
		holeL =  hole.movex(mm(-0.25)).union(hole.movex(mm(-centerDistanceOfLatch-0.25)))
					
	}
	int holesPerSide = 7
	double spacing = (i*panelMaxWidth)//+ (10*i)
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
			 		.difference( ulHole)
			 		.difference( lrHole)
					.difference( llHole)
	//if(i==(panelsPerSide-1))
	//	shape=shape .movex((-panelMaxWidth)- (10))
	//else
	ShapeWithHoles.addExportFormat("svg")
	ShapeWithHoles.setName(i+" panel withHoles")	
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
