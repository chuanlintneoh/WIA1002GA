import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class PasswordHashing {
    public static String hashPassword(String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] passwordBytes = password.getBytes();
            byte[] hashedBytes = messageDigest.digest(passwordBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b: hashedBytes){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}
