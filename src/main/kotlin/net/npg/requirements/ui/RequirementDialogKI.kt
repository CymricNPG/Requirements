package net.npg.requirements.ui

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBList
import net.npg.requirements.configuration.ConfigurationService
import net.npg.requirements.i18n.I18nService
import java.awt.*
import java.nio.file.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.swing.*

/*
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.project.Project
import javax.swing.*

class RequirementDialog(private val project: Project) : DialogWrapper(project) {
    init {
        init()
        title = "New Requirement"
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(GridLayout(8, 2))
        panel.add(JLabel("ID:"))
        panel.add(JTextField("REQ-123"))
        // Add other fields...
        return panel
    }
}
 */

class RequirementDialogKI(
    private val configService: ConfigurationService,
    private val i18nService: I18nService,
    private val idService: IdService,
    private val parentComponent: Component? = null
) : JDialog() {

    private val config = configService.getConfiguration(Path.of("."))!!
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var isOkPressed = false

    // UI-Komponenten
    private val idField = JTextField(20)
    private val summaryField = JTextField(20)
    private val typeCombo = ComboBox<String>()
    private val statusCombo = ComboBox<String>()
    private val priorityCombo = ComboBox<String>()
    private val responsibleCombo = ComboBox<String>()
    private val labelsList = JBList<String>().apply { selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION }
    private val createdField = JTextField(10)
    private val okButton = JButton(i18nService.getString("ok"))
    private val cancelButton = JButton(i18nService.getString("cancel"))

    init {
        title = i18nService.getString("requirement.dialog.title")
        layout = GridBagLayout()
        isModal = true
        defaultCloseOperation = DISPOSE_ON_CLOSE

        // ID-Feld (automatisch vorbelegt)
        idField.text = idService.generateId("dummy/path") // TODO: Pfad dynamisch setzen
        idField.document.addDocumentListener(object : javax.swing.event.DocumentListener {
            override fun insertUpdate(e: javax.swing.event.DocumentEvent?) {
                validateOkButton()
            }

            override fun removeUpdate(e: javax.swing.event.DocumentEvent?) {
                validateOkButton()
            }

            override fun changedUpdate(e: javax.swing.event.DocumentEvent?) {
                validateOkButton()
            }
        })

        // Zusammenfassung
        summaryField.document.addDocumentListener(object : javax.swing.event.DocumentListener {
            override fun insertUpdate(e: javax.swing.event.DocumentEvent?) {
                validateOkButton()
            }

            override fun removeUpdate(e: javax.swing.event.DocumentEvent?) {
                validateOkButton()
            }

            override fun changedUpdate(e: javax.swing.event.DocumentEvent?) {
                validateOkButton()
            }
        })

        // Dropdowns füllen
        typeCombo.model = DefaultComboBoxModel(config.types.values.toTypedArray())
        statusCombo.model = DefaultComboBoxModel(config.states.values.toTypedArray())
        priorityCombo.model = DefaultComboBoxModel(config.priorities.values.toTypedArray())
        responsibleCombo.model = DefaultComboBoxModel(config.members.values.toTypedArray())
        labelsList.setListData(config?.labels?.values?.toTypedArray())

        // Aktuelles Datum setzen
        createdField.text = LocalDate.now().format(formatter)
        createdField.isEnabled = false

        // Buttons
        okButton.isEnabled = false
        okButton.addActionListener { isOkPressed = true; dispose() }
        cancelButton.addActionListener { dispose() }

        // Layout
        val gbc = GridBagConstraints().apply {
            insets = Insets(4, 4, 4, 4)
            fill = GridBagConstraints.HORIZONTAL
        }

        // Felder hinzufügen
        gbc.gridx = 0; gbc.gridy = 0; add(JLabel(i18nService.getString("id"))); gbc.gridx = 1; add(idField)
        gbc.gridx = 1; gbc.gridy = 0; add(JLabel(i18nService.getString("summary"))); gbc.gridx = 1; add(summaryField)
        gbc.gridx = 2; gbc.gridy = 0; add(JLabel(i18nService.getString("type"))); gbc.gridx = 1; add(typeCombo)
        gbc.gridx = 3; gbc.gridy = 0; add(JLabel(i18nService.getString("status"))); gbc.gridx = 1; add(statusCombo)
        gbc.gridx = 4; gbc.gridy = 0; add(JLabel(i18nService.getString("priority"))); gbc.gridx = 1; add(priorityCombo)
        gbc.gridx = 5; gbc.gridy = 0; add(JLabel(i18nService.getString("responsible"))); gbc.gridx = 1; add(responsibleCombo)
        gbc.gridx = 6; gbc.gridy = 0; add(JLabel(i18nService.getString("labels"))); gbc.gridx = 1; add(JScrollPane(labelsList))
        gbc.gridx = 7; gbc.gridy = 0; add(JLabel(i18nService.getString("created"))); gbc.gridx = 1; add(createdField)

        // Buttons
        val buttonPanel = JPanel(FlowLayout(FlowLayout.RIGHT)).apply {
            add(okButton)
            add(cancelButton)
        }
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; add(buttonPanel, gbc)

        pack()
        setLocationRelativeTo(parentComponent)
    }

    private fun validateOkButton() {
        okButton.isEnabled = idField.text.isNotBlank() && summaryField.text.isNotBlank()
    }

    fun showDialog(): Requirement? {
        isVisible = true
        return if (isOkPressed) {
            Requirement(
                id = idField.text,
                summary = summaryField.text,
                type = typeCombo.selectedItem as? String,
                status = statusCombo.selectedItem as? String,
                priority = priorityCombo.selectedItem as? String,
                responsible = responsibleCombo.selectedItem as? String,
                labels = labelsList.selectedValuesList,
                created = createdField.text
            )
        } else {
            null
        }
    }
}

data class Requirement(
    var id: String = "",
    var summary: String = "",
    var type: String? = null,
    var status: String? = null,
    var priority: String? = null,
    var responsible: String? = null,
    var labels: List<String> = emptyList(),
    var created: String = "", // Format: yyyy-MM-dd
    var references: List<String> = emptyList()
)