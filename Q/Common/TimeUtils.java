package Common;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * Contains functionality to run methods with a timeout and
 */
public class TimeUtils {

  /**
   * Pauses runtime for a given number of seconds.
   */
  public static void catchBreath(int seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    }
    catch (Exception e) {
      e.getMessage();
    }
  }

  public static <T> T callWithTimeOut(Supplier<T> call, int timeout)
          throws InterruptedException, ExecutionException, TimeoutException {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    try {
      Future<T> future = executorService.submit(call::get);
      T result = future.get(timeout, TimeUnit.SECONDS);
      return result;
    } finally {
      executorService.shutdown();
    }
  }
}
