import java.lang.reflect.*;
import java.util.*;
import java.lang.*;

// Kevin Ng - 30029178

public class Inspector {
	
	//initial method to get the object's class
    public void inspect(Object obj, boolean recursive) {
    	Class c = obj.getClass();
		inspectClass(c, obj, recursive, 0);
    }

    // Goes through the class and reflect it
    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
    	// saves variable class
    	Class cls = c;
    	
    	// if class exist
    	if(cls != null) {
    		//print out class name and it's interfaces
    		System.out.println("Class: " + cls.getName());
    		Class[] interfaces = cls.getInterfaces();
    		System.out.println("Interface: " + Arrays.asList(interfaces));
    		System.out.println("---------------------------");
    		
    		// perform inspections on the following parts of a class
    		inspectSuper(cls);
			
			inspectConstructor(cls);
			
    		if(c.getDeclaredFields().length > 0) {
    			inspectFields(cls, obj, recursive);
    		}
    		if(c.getDeclaredMethods().length > 0) {
    			inspectMethods(cls);
    		}
    	}
    	
    	
    }

    // print out super class name and it's interfaces
	private void inspectSuper(Class cls) {
		Class[] interfaces;
		Class superClass = cls.getSuperclass();
		
		System.out.println("Superclass: " + superClass.getSimpleName());
		interfaces = superClass.getInterfaces();
		System.out.println("Interface: " + Arrays.asList(interfaces));
		
		// print out superclass' superclass' name and it's interfaces if they exist
		while(superClass.getName().compareTo("java.lang.Object") != 0) {
			superClass = superClass.getSuperclass();
			System.out.println("---------------------------");
			System.out.println("Superclass: " + superClass.getSimpleName());
			interfaces = superClass.getInterfaces();
			System.out.println("Interface: " + Arrays.asList(interfaces));
		}
	}

    // gets a class constructor and prints it's following info
    private void inspectConstructor(Class cls) {
		Constructor[] constructs = cls.getDeclaredConstructors();
		for(Constructor construct :constructs) {
			System.out.println("---------------------------------------");
			System.out.println("Constructor: " + construct.toString());
			System.out.println("- Name: " + construct.getName());
			
			// prints out each parameter type and name
			System.out.println("- Parameters: ");
			Parameter[] params = construct.getParameters();
			for(Parameter param: params) {
				System.out.println("	- " + param.getType() + " " + param.getName());
			}
			
			System.out.println("- Modifiers: " + Modifier.toString(construct.getModifiers()));
		}
	}
    
    // performs inspection on the class' fields
	private void inspectFields(Class cls, Object obj, boolean rec) {
    	Field[] flds = cls.getDeclaredFields();
    	for(Field fld :flds) {
    		System.out.println("---------------------------------------");
    		System.out.println("Field: " + fld.toString());
			System.out.println("- Name: " + fld.getName());
			System.out.println("- Type: " + fld.getType());
			System.out.println("- Modifiers: " + Modifier.toString(fld.getModifiers()));
			
			//accesses an object's modifier and get their reference value
			if (!Modifier.isPublic(fld.getModifiers())){
				fld.setAccessible(true);
			}
			String fldType = fld.getType().toString();
			try{
				if (fldType.compareTo("int") == 0){
					System.out.println("	- int: " + fld.getInt(obj));
				} 
				else if (fldType.compareTo("double") == 0){
					System.out.println("	- double: " + fld.getDouble(obj));
				}
				else if (fldType.compareTo("boolean") == 0){
					System.out.println("	- boolean: " + fld.getBoolean(obj)); 
				}
				else {
					if (!rec){
						int hash = System.identityHashCode(obj);
						System.out.println(hash);
						System.out.println("	- object: " + Integer.toHexString(hash));
					}
				}

			} catch (IllegalAccessException error){
				
			}
    	}
    }
    
	// performs inspection the class' methods
    private void inspectMethods(Class cls) {
    	Method[] methods = cls.getDeclaredMethods();
		for(Method method :methods) {
			System.out.println("---------------------------------------");
			System.out.println("Method: " + method.toString());
			System.out.println("- Name: " + method.getName());
			System.out.println("- Exceptions: " + Arrays.asList(method.getExceptionTypes()));
			
			// prints out each parameter type and name
			System.out.println("- Parameters: ");
			Parameter[] params = method.getParameters();
			for(Parameter param: params) {
				System.out.println("	- " + param.getType() + " " + param.getName());
			}
			
			System.out.println("- Returmethodype: " + method.getReturnType());
			System.out.println("- Modifiers: " + Modifier.toString(method.getModifiers()));
		}
    }
}