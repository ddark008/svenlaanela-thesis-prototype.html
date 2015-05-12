package typesafe;

import org.zeroturnaround.javassist.annotation.Modify;
import org.zeroturnaround.javassist.annotation.Patches;

import sample.SampleClass;
import sample.SampleClass_Mirror;

@Patches(SampleClass.class)
public class BeforeMethod extends SampleClass_Mirror {
  @Modify
  protected String instanceMethod(String $1) {
    if ($1 == null) {
      return null;
    }
    return super.instanceMethod($1);
  }
}
