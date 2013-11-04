package mccarthy.brian.dte.nac;

import java.io.File;
import java.io.FileInputStream;

/**
 * This is a custom class loader to load plugins 
 *
 * @author Brian McCarthy
 */
public final class PluginClassLoader extends ClassLoader {

	private File dir;

	public PluginClassLoader (File dir) {
		super(PluginClassLoader.class.getClassLoader());
		System.out.println("Parent: " + PluginClassLoader.class.getClassLoader());
		this.dir = dir;
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("fC: " + name);
		Class<?> c = null;

		File plugin = new File(dir, name.replace('.', File.separatorChar) + ".class");
		int length = (int) plugin.length();
		if (!plugin.exists()) {
			throw new ClassNotFoundException("Class " + name + " not found!");
		}
		byte[] data = new byte[length];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(plugin);
			fis.read(data, 0, length);
			c = defineClass(name, data, 0, length);
			System.out.println("defineClass: " + name);
		} catch (Exception e) {
			throw new ClassNotFoundException("Class " + name + " not found by PluginClassLoader!");
		} finally {
			try {
				fis.close();
			} catch (Exception e) { }
		}
		return c;
	}

	public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		System.out.println("lC: " + name);
		Class<?> c = null;
		c = findLoadedClass(name);
		if (c == null) {
			System.out.println("c == null 1");
			try {
				c = getParent().loadClass(name);
			} catch (Exception e) { }
		}
		if (c == null) {
			System.out.println("c == null 2");
			try {
				c = findClass(name);
			} catch (Exception e) { }
		}
		if (c == null) {
			System.out.println("c == null 3");
			// The parent could not find the class, not could our loader
			throw new ClassNotFoundException("Class " + name + " not found!");
		}
		if (resolve) {
			System.out.println("r");
			resolveClass (c);
		}
		return c;
	}

}
