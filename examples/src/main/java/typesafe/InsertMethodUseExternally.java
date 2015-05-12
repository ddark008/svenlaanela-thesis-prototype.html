package typesafe;

import org.zeroturnaround.javassist.annotation.Modify;
import org.zeroturnaround.javassist.annotation.Patches;

import sample.OtherClass;
import sample.OtherClass_Mirror;
import sample.SampleClass;
import sample.SampleClass_Mirror;

@Patches(SampleClass.class)
public class InsertMethodUseExternally extends SampleClass_Mirror {
  public String trim(String input) {
    if (input == null) {
      return null;
    }
    return input.trim();
  }
}

@Patches(OtherClass.class)
class UseAddedMethodInOtherClass extends OtherClass_Mirror {
  @Modify
  public String otherMethod(String $1) {
    $1 = ((InsertMethodUseExternally) sampleClass).trim($1);
    return super.otherMethod($1);
  }
}