import com.sun.jna.Library;
import com.sun.jna.Native;

public class KeyboardManager {

    static boolean windows = System.getProperty("os.name").toLowerCase().contains("win");

    // static void prepararTecladoMacLinux() {
    //     if (tecladoPreparado) {
    //         return;
    //     }
    //     tecladoPreparado = true;
    //     executarStty("-icanon -echo -icrnl min 1"); // -icrnl: ENTER vira 13, igual ao Windows
    //     Runtime.getRuntime().addShutdownHook(new Thread(() -> executarStty("sane")));
    // }

    public static boolean pressionouTecla() {
        if (windows) {
            // return Crt.INSTANCE._kbhit() != 0;
        }
        // prepararTecladoMacLinux();
        try {
            return System.in.available() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
