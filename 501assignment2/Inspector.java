import java.lang.reflect.*;
import java.util.*;
import java.lang.*;


public class Inspector {
	
    public void inspect(Object obj, boolean recursive) {
    	Class c = obj.getClass();
		inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
    	// saves variable class
    	Class cls = c;
    	
    	// if class exist
    	if(c != null) {
    		//print out class name
    		System.out.println("Class " + cls.getName());
    		
    		System.out.println("---------------------------");
    		
    		// print out super class name
    		Class sup = cls.getSuperclass();
			System.out.println("Superclass: " + sup.getSimpleName());
			
			// print out superclass' superclass' name if they exist
			int multiSupCount = 0;
			while(sup.getName().compareTo("java.lang.Object") != 0) {
				multiSupCount ++;
				sup = sup.getSuperclass();
				System.out.println("Superclass: " + sup.getSimpleName());
			}
			
			System.out.println("---------------------------");
			
			// prints out interfaces
			Class[] interfaces = cls.getInterfaces();
			System.out.println("Interface: " + Arrays.asList(interfaces));
			if(multiSupCount > 0) {
				for(int i = 0; i < multiSupCount; i++) {
					sup = cls.getSuperclass();
					System.out.println("Interface: " + Arrays.asList(sup.getInterfaces()));
					System.out.println(sup.getName());
				}
			}
			
			inspectConstructor(cls);
			
    		if(c.getDeclaredFields().length > 0) {
    			inspectFields(cls, obj, recursive);
    		}
    		if(c.getDeclaredMethods().length > 0) {
    			inspectMethods(cls);
    		}
    	}
    	
    	
    }

    // gets a class constructor and prints it's following info
    private void inspectConstructor(Class cls) {
		Constructor[] cnstrs = cls.getDeclaredConstructors();
		for(Constructor cnstr :cnstrs) {
			System.out.println("---------------------------------------");
			System.out.println("Constructor: " + cnstr.toString());
			System.out.println("- Name: " + cnstr.getName());
			System.out.println("- Parameters: ");
			Parameter[] params = cnstr.getParameters();
			for(Parameter p: params) {
				System.out.println("	- " + p.getType() + " " + p.getName());
			}
			System.out.println("- Modifiers: " + Modifier.toString(cnstr.getModifiers()));
		}
	}
    
    
	private void inspectFields(Class cls, Object obj, boolean rec) {
    	Field[] flds = cls.getDeclaredFields();
    	for(Field fld :flds) {
    		System.out.println("---------------------------------------");
    		System.out.println("Field: " + fld.toString());
			System.out.println("- Name: " + fld.getName());
			System.out.println("- Type: " + fld.getType());
			System.out.println("- Modifiers: " + Modifier.toString(fld.getModifiers()));
			if (!Modifier.isPublic(fld.getModifiers())){
				fld.setAccessible(true);
			}
			String fldType = fld.getType().toString();
			try{
				if (fldType.compareTo("boolean") == 0){
					System.out.println("- boolean: " + fld.getBoolean(obj));	
				} else if (fldType.compareTo("int") == 0){
					System.out.println("- int: " + fld.getInt(obj));
				} else if (fldType.compareTo("double") == 0){
					System.out.println("- double: " + fld.getDouble(obj));
				} else {
					if (rec == false){
						int hash = System.identityHashCode(obj);
						System.out.println("- object: " + Integer.toHexString(hash));
					}	
				}

			} catch (IllegalAccessException error){
				
			}
    	}
    }
    
    private void inspectMethods(Class cls) {
    	Method[] mtds = cls.getDeclaredMethods();
		for(Method mtd :mtds) {
			System.out.println("---------------------------------------");
			System.out.println("Method: " + mtd.toString());
			System.out.println("- Name: " + mtd.getName());
			System.out.println("- Exceptions: " + Arrays.asList(mtd.getExceptionTypes()));
			
			System.out.println("- Parameters: ");
			Parameter[] params = mtd.getParameters();
			for(Parameter p: params) {
				System.out.println("	- " + p.getType() + " " + p.getName());
			}
			System.out.println("- Return type: " + mtd.getReturnType());
			System.out.println("- Modifiers: " + Modifier.toString(mtd.getModifiers()));
		}
    }
}