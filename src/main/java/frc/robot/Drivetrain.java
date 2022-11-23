package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Joystick;

public class Drivetrain {
    int LEFT_MOTOR_ID = 4;
    int RIGHT_MOTOR_ID = 2;
    MotorType AC = MotorType.kBrushless;
    int CONTROLLER_ID = 0;
    //double outputT = 0;
    double outputD = 0;
    double ramp = 0.2;
    double speed = 0;
    double turn = 0;
    
    //Joystick
    Joystick controller = new Joystick(CONTROLLER_ID);
    
    //Motor
    CANSparkMax left_motor = new CANSparkMax(LEFT_MOTOR_ID, AC);
    CANSparkMax right_motor = new CANSparkMax(RIGHT_MOTOR_ID, AC);

    private static Drivetrain m_drivetrain = null;
    private static final int k_freeCurrentLimit = 40;
    private static final int k_stallCurrentLimit = 40;

    public Drivetrain(){
        left_motor.restoreFactoryDefaults();
        right_motor.restoreFactoryDefaults();

        left_motor.setIdleMode(IdleMode.kBrake);
        right_motor.setIdleMode(IdleMode.kBrake);
  
        right_motor.setSmartCurrentLimit(k_stallCurrentLimit, k_freeCurrentLimit);
        left_motor.setSmartCurrentLimit(k_stallCurrentLimit, k_freeCurrentLimit);
    }

    public static Drivetrain getInstance(){
        if(m_drivetrain == null)
        m_drivetrain = new Drivetrain();

        return m_drivetrain;
    } 
    
    public void arcadeDrive(double speed, double turn){
        //Config Turn and Speed Rates here:
        double tfactor = 0.3;
        double sfactor = 0.2;

        turn = tfactor * deadband(turn);
        speed = sfactor * deadband(speed);
  
        outputD = outputD + (outputD - speed) * -ramp;
        //outputT = outputT + (outputT - turn) * -ramp * 1.1;
      
        left_motor.set(outputD-turn);
        right_motor.set(outputD+turn);
    }

    //Deadband
    double deadband(double value) {
        /* Upper deadband */
        if (value >= +0.10 ) 
            return value-0.1;
        /* Lower deadband */
        if (value <= -0.10)
            return value+0.1;
    
        /* Outside deadband */
            return 0;
    }
}