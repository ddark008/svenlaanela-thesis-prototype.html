package typesafe;

import org.zeroturnaround.javassist.annotation.Modify;
import org.zeroturnaround.javassist.annotation.Patches;

import sample.SampleClass;
import sample.SampleClass_Mirror;

@Patches(SampleClass.class)
public class AfterMethod extends SampleClass_Mirror {
  @Modify
  protected String instanceMethod(String input) {
    return super.instanceMethod(input) + "!";
  }
}