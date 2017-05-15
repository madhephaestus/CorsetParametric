import com.piro.bezier.BezierPath;

// horizontal
bustSize 		= new LengthParameter("Bust Size",30,[120.0,1.0])
underbust		= new LengthParameter("underbust",30,[120.0,1.0])
waist 		= new LengthParameter("waist",30,[120.0,1.0])
highHip		= new LengthParameter("high hip",30,[120.0,1.0])
lowHip 		= new LengthParameter("low hip",30,[120.0,1.0])
// verticals
upBreast 		= new LengthParameter("up breast",30,[120.0,1.0])
downbreast	= new LengthParameter("down breast",30,[120.0,1.0])
midBreast		= new LengthParameter("middle of breast",30,[120.0,1.0])
uBreastToWaist	= new LengthParameter("under breast to waist",30,[120.0,1.0])
waistToPubic 	= new LengthParameter("waist to pubic bone",30,[120.0,1.0])
waistHighHip	= new LengthParameter("waist high hip",30,[120.0,1.0])
waistLowHip	= new LengthParameter("waist low hip",30,[120.0,1.0])
waistBackTop	= new LengthParameter("waist to top back",30,[120.0,1.0])
waistBackBottom= new LengthParameter("waist to bottom back",30,[120.0,1.0])
// construction
numPanels	= new LengthParameter("number of panels",6,[12,4])

public static CSG byPath(List<List<Vector3d>> points, double height) {

	return byPath(points, height, 200);

}

public static CSG byPath(List<List<Vector3d>> points, double height, int resolution) {
	ArrayList<Transform> trPath = pathToTransforms(points, resolution);
	List<Vector3d> finalPath = new ArrayList<>();
	for (Transform tr : trPath) {
		javax.vecmath.Vector3d t1 = new javax.vecmath.Vector3d();
		tr.getInternalMatrix().get(t1);
		Vector3d finalPoint = new Vector3d(t1.x , t1.y , 0);
		finalPath.add(finalPoint);
	}
	return Extrude.points(new Vector3d(0, 0, height), finalPath);
}
	public static ArrayList<Transform> pathToTransforms(List<List<Vector3d>> points, int resolution){
		String pathStringA = "";
		String pathStringB = "";
		
		ArrayList<Double> start = (ArrayList<Double>) Arrays.asList((double) 0, (double) 0, (double) 0);
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
		println "A string = " +pathStringA
		println "B String = " +pathStringB
		BezierPath path = new BezierPath();
		path.parsePathString(pathStringA);
		BezierPath path2 = new BezierPath();
		path2.parsePathString(pathStringB);
		
		return Extrude.bezierToTransforms(path, path2, resolution);
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
public static ArrayList<Transform> bezierToTransforms(Vector3d start, Vector3d controlA, Vector3d controlB,
		Vector3d endPoint, int iterations) {
	BezierPath path = new BezierPath();
	path.parsePathString("M "+start.x+","+start.y+"\n"+
			"C " + controlA.x + "," + controlA.y + " " + controlB.x + "," + controlB.y + " "
			+ endPoint.x + "," + endPoint.y);
	BezierPath path2 = new BezierPath();
	path2.parsePathString("M "+start.x+","+start.z+"\n"+
			"C " + controlA.x + "," + controlA.z + " " + controlB.x + "," + controlB.z + " "
			+ endPoint.x + "," + endPoint.z);

	return Extrude.bezierToTransforms(path, path2, iterations);
}

public static CSG profileWithHoles(List<List<Vector3d>> profile){
	CSG hole =  new Cube(1,1,30).toCSG()
				.movey(-3)
	
	def holes = 	CSG.unionAll(	moveAlongProfile(hole,profile,40)		)
	CSG shape= byPath(profile,5)
			.difference(holes)

	return shape
}

List<Vector3d> sideProfile =[ new Vector3d(0,100,0),
			new Vector3d(0,100,0),
			new Vector3d(90,90,0),
			new Vector3d(100,100,0)]
List<Vector3d> sideProfile1 =[	new Vector3d(0,0,0),
			new Vector3d(30,30,0),
			new Vector3d(-30,70,0),
			new Vector3d(0,100,0)]
List<List<Vector3d>>  profile = [
		sideProfile1,
		[new Vector3d(0,100,0)],
		sideProfile,
		[new Vector3d(100,0,0)],
		[new Vector3d(0,0,0)]
]

CSG shape = byPath(profile,5)
CSG hole =  new Cube(1,1,30).toCSG()
				.movey(-3)
def holeParts = Extrude.move(hole,bezierToTransforms(sideProfile1,  20))
//holeParts.remove(holeParts.size()-1)
holeParts.remove(0)

shape=shape.difference(holeParts)

return [shape]