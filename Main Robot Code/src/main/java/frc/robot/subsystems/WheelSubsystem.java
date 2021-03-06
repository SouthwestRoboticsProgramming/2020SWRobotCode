package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

/*
* Wheel subsystem includes all motors, solenoids, and sensors assosiated with spinning the wheel of fortune.
*/
public class WheelSubsystem extends SubsystemBase {

  private final WPI_TalonSRX spinnerTalon;
  private final DoubleSolenoid wheelDoubleSolenoid;
  private DigitalInput limitSwitch;
  // private final int spinnerTalonPort = 9;
  // private final int encoderTicks = 4096;

  public enum Color{
    red,blue,green,yellow, noColor
  } 
  
  public WheelSubsystem() {
    spinnerTalon = new WPI_TalonSRX(Constants.wheelOfFortuneTalonPort);
    limitSwitch = new DigitalInput(Constants.wheelOfFortuneLimitPort);
    wheelDoubleSolenoid = new DoubleSolenoid(Constants.PCMID, Constants.pushSolenoidPort, Constants.retractSolenoidPort);
    wheelDoubleSolenoid.set(Value.kOff);

    spinnerTalon.configNeutralDeadband(0.001);
    spinnerTalon.setNeutralMode(NeutralMode.Brake);
    spinnerTalon.setInverted(false);

    spinnerTalon.config_kP(0, 0);
    spinnerTalon.config_kI(0, 0);
    spinnerTalon.config_kD(0, 0);
    spinnerTalon.config_kF(0, 0);
  }

  //Spinner talon SRX
  public void setSpinnerTalon(double x){
    spinnerTalon.set(ControlMode.PercentOutput, x);
    Robot.shuffleBoard.wheelSpinnerOutput.setDouble(spinnerTalon.getMotorOutputPercent());
  }

  public double getSpinnerTalon(){
    return spinnerTalon.get();
  }

  public boolean getLimit() {
    return limitSwitch.get();
  }

  
  //Push solenoid 
  public void setPushedState(boolean pushed) {
    if (pushed) {
      wheelDoubleSolenoid.set(Value.kForward);
    }
    else {
      wheelDoubleSolenoid.set(Value.kReverse);
    }
    
    //TODO: Readd shuffleboard
    // Robot.shuffleBoard.wheelPushSolenoid.setBoolean(pushSolenoid.get());
    // Robot.shuffleBoard.wheelRetractSolenoid.setBoolean(retractSolenoid.get());
  }


  //Color sensor
  // I2C.Port i2cPort = I2C.Port.kOnboard;
  // ColorSensorV3 cs = new ColorSensorV3(i2cPort);
  // TCA9548A TCA9548A = new TCA9548A();

  // int adress = 0x70;
  // private I2C i2c = new I2C(Port.kOnboard, adress);
  // I2C.Port i2cPort = Port.kOnboard;
  private ColorSensorV3 cs = new ColorSensorV3(Port.kOnboard);

  public Color getColor() {
    int r = cs.getRed();
    int g = cs.getGreen();
    int b = cs.getBlue();
    // int r = 0;
    // int g = 0;
    // int b = 0;
    System.out.println("R = " + r + " G = " + g + " B = " + b);
    if (.7*g < r && r < 1.5*g && 1.5*b < r && r < 3*b) {
      return Color.red;
    } else if (2*r < g && g < 3*r && 1.5*b < g && g < 4*b) {
      return Color.green;
    } else if (1.5*r < b && b < 3*r && (Math.abs(b+g)/2)/b < .3 * b) {
      return Color.blue;
    } else if (1.4*r < g && g < 3*r && 2*b < g && g < 5*b) {
      return Color.yellow;
    } else {
      return Color.noColor;
    }
    // System.out.println("R = " + r + " G = " + g + " B = " + b);
  }

  @Override
  public void periodic() {
    // System.out.println("wheel angle = " + gyro.getAngle());
  }

}
