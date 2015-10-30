package tree;

import com.intellij.ide.IconProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by the Biomatters and the Phyla team for the betterment of mankind.
 */
public class MyIconProvider extends IconProvider {
    @Nullable
    @Override
    public Icon getIcon(PsiElement element, int flags) {
        if(element.textMatches("test")){
            return
        }
        return null;
    }
}
