import com.piro.bezier.BezierPath;
CSGDatabase.clear()
double mm(double input){
	return input*25.4
}
double seamInset = 14
double boningWidth =6

// horizontal
//bustSize 		= new LengthParameter("Bust Size",30,[120.0,1.0])
underbust		= new LengthParameter("underbust",mm(26),[120.0,1.0])
waist 		= new LengthParameter("waist",mm(26),[120.0,1.0])
highHip		= new LengthParameter("high hip",mm(30),[120.0,1.0])
lowHip 		= new LengthParameter("low hip",mm(38),[120.0,1.0])
// verticals
//upBreast 		= new LengthParameter("up breast",30,[120.0,1.0])
//downbreast	= new LengthParameter("down breast",30,[120.0,1.0])
//midBreast		= new LengthParameter("middle of breast",30,[120.0,1.0])
uBreastToWaist	= new LengthParameter("under breast to waist",mm(5.5),[120.0,1.0])
waistToPubic 	= new LengthParameter("waist to pubic bone",mm(7.5),[120.0,1.0])
waistHighHip	= new LengthParameter("waist high hip",mm(4),[120.0,1.0])
//waistLowHip	= new LengthParameter("waist low hip",30,[120.0,1.0])
waistBackTop	= new LengthParameter("waist to top back",mm(8),[120.0,1.0])
waistBackBottom= new LengthParameter("waist to bottom back",mm(9.5),[120.0,1.0])
// construction
numPanels	= new LengthParameter("number of panels",8,[12,4])
static ArrayList<Line3D> showEdges(ArrayList<Vector3d> finalPath,Double offset, javafx.scene.paint.Color color ){
	 ArrayList<Line3D> lines =[]
	for(int i=0;i<finalPath.size();i++){
		
		double z=offset
		Vector3d p1 =finalPath.get(i)
		Vector3d p2 = finalPath.get(i<(finalPath.size()-1)?i+1:0)
		Line3D line = new Line3D(p1,p2);
		line.setStrokeWidth(0.8);
		line.setStroke(color);
		lines .add(line);
		BowlerStudioController.getBowlerStudio() .addNode(line)
	}
	
	return lines
}
public static CSG byPath(List<List<Vector3d>> points, double height) {

	return byPath(points, height, 200);

}

public static CSG byPath(List<List<Vector3d>> points, double height, int resolution) {
	ArrayList<Transform> trPath = pathToTransforms(points, resolution);
	ArrayList<Vector3d> finalPath = new ArrayList<>();
	for (Transform tr : trPath) {
		javax.vecmath.Vector3d t1 = new javax.vecmath.Vector3d();
		tr.getInternalMatrix().get(t1);
		Vector3d finalPoint = new Vector3d(t1.x , t1.y , 0);
		finalPath.add(finalPoint);
	}
	showEdges(finalPath,(double)0.0,javafx.scene.paint.Color.RED)
	println "Path size = " +finalPath.size()
	//List<Polygon> p =  Polygon.fromConcavePoints(finalPath)
	//for(Polygon pl:p)
	//	BowlerStudioController.getBowlerStudio()addObject(pl,null)
	//return new Cube(height).toCSG()
	return Extrude.points(new Vector3d(0, 0, height), finalPath);
}
public static ArrayList<Transform> pathToTransforms(List<List<Vector3d>> points, int resolution){
	
	
	Vector3d start = points.get(0).get(0)
	String pathStringA = "M "+ start.x + "," + start.y;
	String pathStringB = pathStringA;
	
	for (List<Vector3d> sections : points) {
		if (sections.size() == 4) {
		Vector3d controlA= sections.get(1)
		Vector3d controlB= sections.get(2)
		Vector3d endPoint = sections.get(3)
		/*
			ArrayList<Double> controlA = (ArrayList<Double>) Arrays.asList(sections.get(1).x - start.get(0),
					sections.get(1).y - start.get(1), sections.get(1).z - start.get(2));
					
			ArrayList<Double> controlB = (ArrayList<Double>) Arrays.asList(sections.get(2).x - start.get(0),
					sections.get(2).y - start.get(1),  sections.get(2).z - start.get(2));
			;
			ArrayList<Double> endPoint = (ArrayList<Double>) Arrays.asList(sections.get(3).x - start.get(0),
					sections.get(3).y - start.get(1), sections.get(3).z - start.get(2));
			;
			*/
			
			pathStringA+=("C " + controlA.x + "," + controlA.y + " " + controlB.x + ","+ controlB.y + " " + endPoint.x + "," + endPoint.y+"\n");
			pathStringB+=("C " + controlA.x + "," + controlA.z + " " + controlB.x + ","+ controlB.z + " " + endPoint.x + "," + endPoint.z+"\n");
			//start.set(0, sections.get(3).x);
			//start.set(1, sections.get(3).y);
			//start.set(2,sections.get(3).z);
			
		} else if (sections.size() == 1) {
			
			pathStringA+="L " + (double)sections.get(0).x + "," +  (double)sections.get(0).y +"\n";
			pathStringB+="L " + (double)sections.get(0).x + "," +  (double)sections.get(0).z +"\n";
			//start.set(0, sections.get(0).x);
			//start.set(1, sections.get(0).y);
			//start.set(2, sections.get(0).z);
		}
	}
	//println "A string = " +pathStringA
	//println "B String = " +pathStringB
	BezierPath path = new BezierPath();
	path.parsePathString(pathStringA);
	BezierPath path2 = new BezierPath();
	path2.parsePathString(pathStringB);
	
	return bezierToTransforms(path, path2, resolution)
}

public static ArrayList<CSG> moveAlongProfile(CSG object, List<List<Vector3d>> points, int resolution){

	return Extrude.move(object,pathToTransforms(points,  resolution));
}
public static ArrayList<Transform> bezierToTransforms(
	Vector3d controlA, Vector3d controlB,
		Vector3d endPoint, int iterations) {
	BezierPath path = new BezierPath();
	path.parsePathString("C " + controlA.x + "," + controlA.y + " " + controlB.x + ","
			+ controlB.y + " " + endPoint.x + "," + endPoint.y);
	BezierPath path2 = new BezierPath();
	path2.parsePathString("C " + controlA.x + "," + controlA.z + " " + controlB.x + ","
			+ controlB.z + " " + endPoint.x + "," + endPoint.z);

	return bezierToTransforms(path, path2, iterations);
}
public static ArrayList<Transform> bezierToTransforms(List<Vector3d> parts, int iterations) {
	if(parts.size() == 3)
		return bezierToTransforms(parts.get(0), parts.get(1), parts.get(2), iterations);
	if(parts.size() == 2)
		return bezierToTransforms(parts.get(0), parts.get(0), parts.get(1),parts.get(1), iterations);
	if(parts.size() == 1)
		return bezierToTransforms(new Vector3d(0, 0,0) , new Vector3d(0, 0,0), parts.get(0),parts.get(0), iterations);
	return bezierToTransforms(parts.get(0), parts.get(1), parts.get(2),parts.get(3), iterations);
}
public static ArrayList<Transform> bezierToTransforms(BezierPath pathA, BezierPath pathB, int iterations) {
	    ArrayList<Transform> p = new ArrayList<Transform>();
	    Vector3d pointAStart = pathA.eval(0);
	    Vector3d pointBStart = pathB.eval(0);
	    double x = pointAStart.x, y = pointAStart.y, z =  pointBStart.y;
	    double lastx = x, lasty = y, lastz = z;
	    
	    for (int i = 0; i < iterations - 1; i++) {
	        float pathFunction = (float)(((float)i)/((float)(iterations - 1)));
	        Vector3d pointA = pathA.eval(pathFunction);
	        Vector3d pointB = pathB.eval(pathFunction);

	        x = pointA.x;
	        y = pointA.y;
	        z = pointB.y;
	        
	        Transform t = new Transform();
	        t.translateX(x);
	        t.translateY(y);
	        t.translateZ(z);

	        double ydiff = y - lasty;
	        double zdiff = z - lastz;
	        double xdiff = x - lastx;

	        // t.rotX(45-Math.toDegrees(Math.atan2(zdiff,ydiff)))

	        double rise = zdiff;
	        double run = Math.sqrt((ydiff * ydiff) + (xdiff * xdiff));
	        double rotz = 90 - Math.toDegrees(Math.atan2(xdiff, ydiff));
	        double roty = Math.toDegrees(Math.atan2(rise, run));

	        t.rotZ(-rotz);
	        t.rotY(roty);
	        //if(i==0)
	        //    println "  Tr = "+x+" "+y+" "+z
	        System.out.println( "  GR-Tr = "+x+" "+y+" "+z+" path = "+pathFunction);
	        p.add(t);
	        lastx = x;
	        lasty = y;
	        lastz = z;
	    }
	    Vector3d pointA = pathA.eval((float) 1);
	    Vector3d pointB = pathB.eval((float) 1);

	    x = pointA.x;
	    y = pointA.y;
	    z = pointB.y;
	    Transform t = new Transform();
	    t.translateX(x);
	    t.translateY(y);
	    t.translateZ(z);

	    double ydiff = y - lasty;
	    double zdiff = z - lastz;
	    double xdiff = x - lastx;

	    double rise = zdiff;
	    double run = Math.sqrt((ydiff * ydiff) + (xdiff * xdiff));
	    double rotz = 90 - Math.toDegrees(Math.atan2(xdiff, ydiff));
	    double roty = Math.toDegrees(Math.atan2(rise, run));

	    t.rotZ(-rotz);
	    t.rotY(roty);
	    p.add(t);

	    return p;
}
public static ArrayList<Transform> bezierToTransforms(Vector3d start, Vector3d controlA, Vector3d controlB,
		Vector3d endPoint, int iterations) {
	String startString = "M "+start.x+","+start.y
	println "Start = "+startString
	BezierPath path = new BezierPath();
	path.parsePathString(startString+"\n"+
			"C " + controlA.x + "," + controlA.y + " " + controlB.x + "," + controlB.y + " "
			+ endPoint.x + "," + endPoint.y);
	BezierPath path2 = new BezierPath();
	path2.parsePathString("M "+start.x+","+start.z+"\n"+
			"C " + controlA.x + "," + controlA.z + " " + controlB.x + "," + controlB.z + " "
			+ endPoint.x + "," + endPoint.z);

	def parts =  Extrude.bezierToTransforms(path, path2, iterations);
	def newParts =[]
	for(int i=0;iterations!=newParts.size();i++){
		newParts.add(parts.get(i))
	}
	//newParts.remove(parts.size()-1)
	//newParts.remove(0)
	return newParts
}


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
	
	println "Increment A = "+incrementA+" increment b "+incrementB
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
	CSG hole = new Cube(2,2,30).toCSG()
	hole = hole.toYMax()//.toXMax()
			.movey(-seamInset/2-boningWidth/2)
			.union(hole.toYMin()//.toXMin()
			.movey((-seamInset/2)+boningWidth/2)
			)
					
	CSG holeR =  hole
				.movex(mm(0.3))
					
	CSG holeL =  hole
				.movex(mm(-0.3))
					
	if(i==0){
		holeR =  new Cylinder(2,30,(int)10).toCSG()
					.movey(-5)					
					.movez(-15)
	}
	if(i==(panelsPerSide-1)){
		holeL =  hole.movex(mm(-0.5)).union(hole.movex(mm(-1)))
					
	}
	int holesPerSide = 7
	double spacing = (i*panelMaxWidth)+ (10*i)
	println "Loading from lib"
	def llower  =Extrude.bezierToTransforms((List<Vector3d> )leftSideLower, i==(panelsPerSide-1)?5: holesPerSide)
	def rlower  =Extrude.bezierToTransforms((List<Vector3d> )rightSideLower,  holesPerSide)
	def rUpper = Extrude.bezierToTransforms((List<Vector3d> )rightSideUpper,  holesPerSide)
	def lUpper  =Extrude.bezierToTransforms((List<Vector3d> )leftSideUpper, i==(panelsPerSide-1)?4: holesPerSide)
	rlower.remove(0)
	lUpper.remove(0)
	llower.remove(0)
	//rUpper.remove(0)
	def llHole = Extrude.move(holeL,llower).collect{ 
		def moved = it.movex(spacing)
		moved.addExportFormat("svg")
		return moved
	}
	def lrHole = Extrude.move(holeR,rlower).collect{ 
		def moved = it.movex(spacing)
		moved.addExportFormat("svg")
		return moved
	}
	def ulHole = Extrude.move(holeL,lUpper).collect{ 
		def moved = it.movex(spacing)
		moved.addExportFormat("svg")
		return moved
	}
	def urHole = Extrude.move(holeR,rUpper).collect{ 
		def moved = it.movex(spacing)
		moved.addExportFormat("svg")
		return moved
	}

	shape=shape.movex(spacing)
	shape=shape.difference( urHole)
			 .difference( ulHole)
			 .difference( lrHole)
			 .difference( llHole)
	
	//if(i==(panelsPerSide-1))
	//	shape=shape .movex((-panelMaxWidth)- (10))
	//else
		
	shape.addExportFormat("svg")
	panels.add(shape)
	panels.addAll(llHole)
	panels.addAll(lrHole)
	panels.addAll(ulHole)
	panels.addAll(urHole)
	if(i==0){
		
		//Image ruler = AssetFactory.loadAsset("BowlerStudio-Icon.png");
		//ImageView rulerImage = new ImageView(ruler);
		//Slice.slice(shape,slicePlane, 0)
	}
}
return panels
