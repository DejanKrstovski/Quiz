package helpers;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SerializerUtil {

	public SerializerUtil() {
		
	}
	/**
	 * This method serializes an object to a file.
	 * 
	 * @param object The object to serialize.
	 * @param filePath The path of the file where the object will be serialized.
	 * @throws Exception If an error occurs during serialization.
	 */
	public static void saveObject(Object object, String filePath) throws Exception {
		FileOutputStream fileOut = new FileOutputStream(filePath);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		try {
			out.writeObject(object);  // SERIALIZATION
			System.out.println("Object serialized to: " + new java.io.File(filePath).getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Serialization failed: " + e.getMessage());
		} finally {
			out.close();
		}
	}
	
	public static void readObject(String filePath) {
		// This method is not implemented yet.
		// It should read an object from the specified file path.
		System.out.println("Read object from: " + new java.io.File(filePath).getAbsolutePath());
		// Implementation goes here...
	}
}
