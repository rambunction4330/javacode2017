package frc.team4330.screambunction.testingutils;

import jaci.openrio.toast.core.StateTracker;
import jaci.openrio.toast.core.Toast;
import jaci.openrio.toast.core.ToastBootstrap;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class ToastRunner extends BlockJUnit4ClassRunner {

    static {

        Thread myThread = new Thread() {
            @Override
            public void run() {
                String ideType = "eclipse";
                String ideEnvironmentVariable = ideEnvironmentVariable = System.getenv("MYIDE");
                if ( ideEnvironmentVariable != null ) {
                    ideType = ideEnvironmentVariable;
                }
                ToastBootstrap.main(new String[]{"-nogui", "-sim", "--color", "-ide", ideType});
            }
        };
        myThread.setDaemon(true);
        myThread.start();

        // wait for Toast to complete bootup
        try {
            while ( true ) {
                Thread.sleep(10);
                if ( StateTracker.currentState != null ) {
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ToastRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    // register a listener to be called when junit tests are complete so we can shutdown Toast
    @Override
    public void run(RunNotifier notifier){
        notifier.addListener(new TestListener());
        super.run(notifier);
    }

    class TestListener extends RunListener {

        @Override
        public void testRunFinished(Result result) throws Exception {
            Toast.getToast().shutdownSafely();
        }
    }

}
