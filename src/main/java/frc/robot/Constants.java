package frc.robot;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.util.*;
public class Constants {
    public static final class ShootConstants{
        public static final double DEFAULT= 0.5;
        public static final double FEED= 0;
        public static final double SHOOT= 0.3;
        public static final double SPEAKER= 0.7;
    }
    public static final class ClimbConstants{
        public static final double UP= 0.5;
        public static final double DOWN= 0;

    }
    public static final class DriveTrainConstants{
        public static final double ksVolts = 0.20322;
        public static final double kvVoltSecondsPerMeter = 3.343;
        public static final double kaVoltSecondsSquaredPerMeter = 1.0356;
        public static final double kPDriveVel = 2.2662;

        public static final double kTrackWidthMeters = Units.inchesToMeters(23);//config, inches beween wheels


        public static final double kRamseteB = 2;
        public static final double kRamseteXeta = 0.7;

        public static final double kGearRatio = 16;
        public static final double kWheelRadiusInches = 6;

        

        public static final double kLinearDistanceConversionFactor = (Units.inchesToMeters(1/(kGearRatio*2*Math.PI*Units.inchesToMeters(kWheelRadiusInches))*10));
        public static DifferentialDriveKinematics kDriveKinematics= new DifferentialDriveKinematics(kTrackWidthMeters);

    }
}
