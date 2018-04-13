import de.begraph.test.Test;
import de.begraph.view.Begraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL11;

public class Application {

  static void drawTorus(float r, float R, int nsides, int rings) {
    float ringDelta = 2.0f * (float) Math.PI / rings;
    float sideDelta = 2.0f * (float) Math.PI / nsides;
    float theta = 0.0f, cosTheta = 1.0f, sinTheta = 0.0f;
    for (int i = rings - 1; i >= 0; i--) {
      float theta1 = theta + ringDelta;
      float cosTheta1 = (float) Math.cos(theta1);
      float sinTheta1 = (float) Math.sin(theta1);
      GL11.glBegin(GL11.GL_QUAD_STRIP);
      float phi = 0.0f;
      for (int j = nsides; j >= 0; j--) {
        phi += sideDelta;
        float cosPhi = (float) Math.cos(phi);
        float sinPhi = (float) Math.sin(phi);
        float dist = R + r * cosPhi;
        GL11.glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
        GL11.glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi);
        GL11.glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
        GL11.glVertex3f(cosTheta * dist, -sinTheta * dist, r * sinPhi);
      }
      GL11.glEnd();
      theta = theta1;
      cosTheta = cosTheta1;
      sinTheta = sinTheta1;
    }
  }

  public static void main(String[] args) {
    final Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    Composite comp = new Composite(shell, SWT.NONE);
    comp.setLayout(new FillLayout());

    Begraph begraph = new Begraph(comp, Test.getTestgraph(100, 5, 100));

    shell.setText("Begraph Demo");
    shell.setSize(1024, 780);
    shell.open();

    begraph.render();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}
