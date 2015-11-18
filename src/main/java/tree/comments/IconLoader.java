package tree.comments;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class IconLoader {

    private static LoadingCache<String, Image> cache = CacheBuilder.newBuilder().build(new CacheLoader<String, Image>() {
        @Override
        public Image load(String url) throws Exception {
            return ImageIO.read(new URL(url));
        }
    });


    public static void setIcon(String url, final JLabel label) {
        Runnable r = () -> {
            try {
                Image remoteImage = cache.get(url);
                final Image scaledInstance = remoteImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                SwingUtilities.invokeLater(() -> label.setIcon(new ImageIcon(scaledInstance)));
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        };
        new Thread(r, "Image loader").start();
    }


}
