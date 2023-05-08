package com.kappdev.worktracker.tracker_feature.domain.use_case

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class HighlightKeywordsTransformation(
    private val keywords: List<String>,
    private val style: SpanStyle
): VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = highlightIn(text),
            offsetMapping = OffsetMapping.Identity
        )
    }

    private fun highlightIn(text: AnnotatedString): AnnotatedString {
        val annotatedString = AnnotatedString.Builder(text)

        for (keyword in keywords) {
            val indexes = getKeywordIndexes(text, keyword)
            for (index in indexes) {
                if (index != -1) {
                    annotatedString.addStyle(
                        style = style,
                        start = index,
                        end = index + keyword.length
                    )
                }
            }
        }

        return annotatedString.toAnnotatedString()
    }

    private fun getKeywordIndexes(text: AnnotatedString, keyword: String): MutableList<Int> {
        val indexes = mutableListOf<Int>()
        var keywordIndex = text.indexOf(keyword)

        while (keywordIndex != -1) {
            indexes.add(keywordIndex)
            keywordIndex = text.indexOf(keyword, keywordIndex + 1)
        }

        return indexes
    }
}