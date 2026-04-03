
package com.example.douplus.core.format

class FileNameTemplateEngine {
    private val invalidFileChars = Regex("[\\/:*?\"<>|]")

    fun render(template: String, values: Map<String, String?>): String {
        var out = template.ifBlank { "{prefix}_{time}" }
        val merged = linkedMapOf<String, String?>(
            "prefix" to "douplus_export",
            "time" to System.currentTimeMillis().toString(),
        ).apply { putAll(values) }
        merged.forEach { (k, v) ->
            out = out.replace("{$k}", sanitize(v.orEmpty()))
        }
        return sanitize(out).ifBlank { "douplus_export" }
    }

    private fun sanitize(input: String): String {
        return input.replace(invalidFileChars, "_").trim()
    }
}
