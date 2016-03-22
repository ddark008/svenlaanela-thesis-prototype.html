package org.zeroturnaround.javassist.annotation.processor.test.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.tools.JavaFileObject;

public class AdHocCompilationResultsClassLoader extends ClassLoader {
  private final Map<String, JavaFileObject> classes;
  private final Map<String, JavaFileObject> resources;

  public AdHocCompilationResultsClassLoader(Map<String, JavaFileObject> javaFileObjects) {
    Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();
    classes.putAll(javaFileObjects);
    Map<String, JavaFileObject> resources = new HashMap<String, JavaFileObject>();
    for (JavaFileObject jfo : javaFileObjects.values()) {
      String name = jfo.getName();
      if (name.startsWith("/CLASS_OUTPUT/")) {
        name = name.substring(14);
      }
      resources.put(name, jfo);
      name = name.replace("/", ".");
      if (name.endsWith(".class")) {
        name = name.substring(0, name.length()-6);
      }
      classes.put(name, jfo);
    }
    this.classes = classes;
    this.resources = resources;
  }

  @Override
  public URL getResource(String name) {
    if (resources.containsKey(name)) {
      return getParent().getResource("java/lang/Object.class");
    } else {
      return getParent().getResource(name);
    }
  }

  public InputStream getResourceAsStream(String name) {
    JavaFileObject jfo = resources.get(name);
    if (jfo != null) {
      try {
        return jfo.openInputStream();
      } catch (Exception e) {
        return getParent().getResourceAsStream(name);
      }
    }
    return getParent().getResourceAsStream(name);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    JavaFileObject jfo = classes.remove(name);
    if (jfo != null) {
      InputStream jfoInputStream = null;
      try {
        jfoInputStream = jfo.openInputStream();
        byte[] bytes = IOUtils.toByteArray(jfoInputStream);
        return defineClass(name, bytes, 0, bytes.length);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
      finally {
        IOUtils.closeQuietly(jfoInputStream);
      }
    }
    return super.findClass(name);
  }
}