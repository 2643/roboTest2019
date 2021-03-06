package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Robot extends TimedRobot {

  NetworkTable visionTable;
  Joystick joystick = new Joystick(0);

  WPI_TalonSRX l1 = new WPI_TalonSRX(14);
  WPI_TalonSRX l2 = new WPI_TalonSRX(15);
  WPI_TalonSRX r1 = new WPI_TalonSRX(16);
  WPI_TalonSRX r2 = new WPI_TalonSRX(1);

  private static volatile byte state = 0;

  void set(double l, double r) {
    l1.set(l);
    l2.set(l);
    r1.set(-r);
    r2.set(-r);
  }

  @Override
  public void robotInit() {
    visionTable = NetworkTableInstance.getDefault().getTable("vision");
    new Thread(() -> CameraServer.getInstance().startAutomaticCapture()).start();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopPeriodic() {
    if(joystick.getRawButton(1)) {
      state = 2;
    } else if(joystick.getRawButton(1)) {
      state = 1;
    } else {
      state = 0;
    }

    if (state == 0) {
      set(
        -Math.pow(joystick.getRawAxis(1), 5), 
        -Math.pow(joystick.getRawAxis(5), 5)
        );
    } else if(state == 1) {
      set(
        -Math.pow(joystick.getRawAxis(1), 1), 
        -Math.pow(joystick.getRawAxis(5), 1)
        );
    } else if (state == 2) {
      double centerLocation = (visionTable.getEntry("centroid-left-x").getDouble(0)
          + visionTable.getEntry("centroid-right-x").getDouble(0)) / 2;
      boolean valid = visionTable.getEntry("valid").getBoolean(false);
      System.out.println("valid: " + valid + "center: " + centerLocation);
      if (valid) {
        if (centerLocation > 0.1) {
          set(0.3, -0.3);
        } else if (centerLocation < -0.1) {
          set(-0.3, 0.3);
        } else {
          set(0.3, 0.3);
        }
      } else {
        set(0, 0);
      }
    }
  }

  @Override
  public void testPeriodic() {
  }
}
