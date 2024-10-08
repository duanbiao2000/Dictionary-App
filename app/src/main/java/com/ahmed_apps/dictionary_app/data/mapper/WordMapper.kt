package com.ahmed_apps.dictionary_app.data.mapper

import com.ahmed_apps.dictionary_app.data.dto.DefinitionDto
import com.ahmed_apps.dictionary_app.data.dto.MeaningDto
import com.ahmed_apps.dictionary_app.data.dto.WordItemDto
import com.ahmed_apps.dictionary_app.domain.model.Definition
import com.ahmed_apps.dictionary_app.domain.model.Meaning
import com.ahmed_apps.dictionary_app.domain.model.WordItem

/**
 * @author Ahmed Guedmioui
 */

/**
 * 将WordItemDto转换为WordItem对象。
 * 对word、meanings和phonetic字段进行安全调用，并提供默认值。
 *
 * @return 转换后的WordItem对象。
 */
fun WordItemDto.toWordItem() = WordItem(
    word = word ?: "",
    meanings = meanings?.map {
        it.toMeaning()
    } ?: emptyList(),
    phonetic = phonetic ?: ""
)


fun MeaningDto.toMeaning() = Meaning(
    definition = definitionDtoToDefinition(definitions?.get(0)),
    partOfSpeech = partOfSpeech ?: ""
)


fun definitionDtoToDefinition(
    definitionDto: DefinitionDto?
) = Definition(
    definition = definitionDto?.definition ?: "",
    example = definitionDto?.example ?: ""
)














