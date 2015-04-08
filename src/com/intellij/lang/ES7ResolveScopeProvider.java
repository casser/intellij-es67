package com.intellij.lang;

import com.intellij.lang.javascript.AtScriptFileType;
import com.intellij.lang.javascript.TypeScriptFileType;
import com.intellij.lang.javascript.ecmascript6.TypeScriptResolveScopeProvider;
import com.intellij.lang.javascript.library.JSCorePredefinedLibrariesProvider;
import com.intellij.lang.javascript.library.JSLibraryMappings;
import com.intellij.lang.javascript.psi.resolve.JSResolveUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.ResolveScopeManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.CachedValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Set;

/**
 * Created by Sergey on 4/4/15.
 */
public class ES7ResolveScopeProvider extends TypeScriptResolveScopeProvider {
    @NotNull
    public GlobalSearchScope getElementResolveScope(@NotNull PsiElement element) {
        GlobalSearchScope scope=null;
        Project project = element.getProject();
        PsiFile psiFile = element.getContainingFile();
        if(psiFile != null) {
            VirtualFile virtualFile = psiFile.getOriginalFile().getVirtualFile();
            if(virtualFile != null) {
                scope = this.getResolveScope(virtualFile, project);
            }
        }
        if(scope==null){
            scope = JSResolveUtil.getJavaScriptSymbolsResolveScope(project);
        }
        return GlobalSearchScope.getScopeRestrictedByFileTypes(scope, AtScriptFileType.INSTANCE);
    }
    @Nullable
    public GlobalSearchScope getResolveScope(@NotNull VirtualFile file, Project project) {
        if(file.getFileType() != AtScriptFileType.INSTANCE) {
            return null;
        } else if(JSCorePredefinedLibrariesProvider.getJavaScriptCorePredefinedLibraryFiles().contains(file)) {
            return new GlobalSearchScope.FilesScope(project, JSCorePredefinedLibrariesProvider.getJavaScriptCorePredefinedLibraryFiles());
        } else {
            Module module = ProjectRootManager.getInstance(project).getFileIndex().getModuleForFile(file);
            if(module == null) {
                return JSResolveUtil.getJavaScriptSymbolsResolveScope(project);
            } else {
                GlobalSearchScope scopeExcludingJsLibs = (GlobalSearchScope)((CachedValue)JSResolveUtil.ourScopeCache.get(module, null)).getValue();
                GlobalSearchScope.getScopeRestrictedByFileTypes(scopeExcludingJsLibs, AtScriptFileType.INSTANCE);
                return scopeExcludingJsLibs;
            }
        }
    }
}
