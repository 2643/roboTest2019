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


  void set(double l, double r) {
    l1.set(-l);
    l2.set(-l);
    r1.set(r);
    r2.set(r);
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
    double centerLocation = (
      visionTable.getEntry("centroid-left-x").getDouble(0) +
      visionTable.getEntry("centroid-right-x").getDouble(0)) / 2;
    boolean valid = visionTable.getEntry("valid").getBoolean(false);
    if(valid) {
      if(centerLocation > 0.1) 
      {
        set(-0.3, 0.3);
      } else if(centerLocation > -0.1)
      {
        set(-0.3, 0.3);
      } else {
        set(0.3, 0.3);
      }
    } else {
      set(0,0);
    }
    
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testPeriodic() {
    set(joystick.getRawAxis(1), joystick.getRawAxis(5));
  }
}
