package fakes.view

import fakes.Config
import fakes.codegen.impl.generateFake
import com.intellij.codeInsight.actions.OptimizeImportsProcessor
import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.codeInspection.actions.CleanupAllIntention
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.findDocument
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import liveplugin.show

class CreateFakeComponentIntention : PsiElementBaseIntentionAction() {
    override fun isAvailable(
        project: Project,
        editor: Editor?,
        element: PsiElement
    ): Boolean {
        val klass = element.parent

        if (klass !is KtClass) return false
        if (klass.parent !is KtFile) return false

        return klass.isInterface() && klass.name?.contains("Component") == true
    }

    override fun getFamilyName() = "CreateFakeComponentIntention"
    override fun getText() = "Generate fake component"

    override fun invoke(
        project: Project,
        editor: Editor?,
        element: PsiElement
    ) {
        val klass = element.parent

        if (klass !is KtClass) return

        val ktFile = klass.parent as? KtFile ?: return
        val className = Config.getFakeComponentName(klass.name ?: return)
        val resultFileName = "$className.kt"

        val generatedCode = try {
            generateFake(klass)
        } catch (t: Throwable) {
            Messages.showInfoMessage(
                project,
                "Error",
                "$t",
            )
            return
        }

        val directory = ktFile.containingDirectory ?: return

        val psiFile = directory.findFile(resultFileName)?.let {
            it.virtualFile?.findDocument()?.apply {
                WriteCommandAction.runWriteCommandAction(project) {
                    setReadOnly(false)
                    setText(generatedCode)
                    PsiDocumentManager.getInstance(project).commitDocument(this)
                }
            }
            it
        } ?: runWriteAction {
            PsiFileFactory.getInstance(project).createFileFromText(
                resultFileName,
                KotlinFileType.INSTANCE,
                generatedCode
            ).apply(directory::add)
        }

        runWriteAction {
            applyCodeQualityUpgrades(
                project,
                editor,
                psiFile
            )
        }
    }

    private fun applyCodeQualityUpgrades(
        project: Project,
        editor: Editor?,
        file: PsiFile
    ) {
        ReformatCodeProcessor(
            project,
            file,
            null,
            false
        ).run()

        OptimizeImportsProcessor(
            project,
            file,
        ).run()

        CleanupAllIntention.INSTANCE.invoke(
            project,
            editor,
            file
        )
    }
}
