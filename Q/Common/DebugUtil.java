package Common;

public class DebugUtil {
  public static void debug(boolean isQuiet, String msg) {
    if (!isQuiet) System.err.println(msg);
  }
}
