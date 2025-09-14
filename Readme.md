# Requirements Management Plugin for IntelliJ IDEA

An IntelliJ IDEA plugin for file-based requirements management using AsciiDoc format.

## What it does

This plugin helps teams manage requirements directly within their IntelliJ development environment using a simple file-based approach. Each requirement is stored as an individual AsciiDoc file, organized in a hierarchical folder structure.

## Key Features

- **File-based storage**: Requirements stored as `.adoc` files with automatic ID generation
- **Visual editors**: GUI for creating requirements and tabular view for overview
- **Smart references**: Link requirements using `<<type:ID>>` syntax with auto-completion
- **Real-time validation**: Instant checking of reference validity and consistency
- **Document generation**: Export to PDF/HTML with proper reference resolution
- **Multilingual support**: English and German interface
- **Integration**: Works with existing AsciiDoc plugin and Git workflow

## Quick Start

1. Install the plugin from JetBrains Plugin Repository
2. Create a new project using the "Requirements Analysis" template
3. Configure your project settings in `config.yaml`
4. Start creating requirements with the Requirements Assistant

## Development Status

This is an active development project. See `docs/Plan.md` for the complete roadmap and current phase information.

## Requirements

- IntelliJ IDEA 2025.1.4.1+
- Java SDK 21
- AsciiDoc plugin (for full editing experience)

## License

GPL-3.0 - See [LICENSE](LICENSE) for details.

---

*Seamlessly integrate requirements management into your IntelliJ workflow.*