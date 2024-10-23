#include <jni.h>
#include <stdio.h>
#include <string.h>

JNIEXPORT jstring JNICALL
Java_com_example_src_ui_theme_screens_NativeLib_parseSchedule(JNIEnv* env, jobject obj, jstring input) {
    const char *nativeString = (*env)->GetStringUTFChars(env, input, 0);

    // Массив для отображения коротких форм дней недели в полные
    const char *days[][2] = {
            {"Пн", "Понедельник"},
            {"Вт", "Вторник"},
            {"Ср", "Среда"},
            {"Чт", "Четверг"},
            {"Пт", "Пятница"},
            {"Сб", "Суббота"},
            {"Вс", "Воскресенье"}
    };

    char result[1024] = "I'm from C\n• "; // Начинаем с сообщения "I'm from C" и символа "•"
    const char *day_ptr;
    int i;

    // Начало обработки строки
    const char *line = strtok((char *) nativeString, "\n");

    while (line != NULL) {
        // Парсим дни недели
        for (i = 0; i < 7; i++) {
            day_ptr = strstr(line, days[i][0]);
            if (day_ptr != NULL) {
                strcat(result, days[i][1]);
                strcat(result, ", ");
            }
        }

        // Найдем время
        char *time_start = strstr(line, "-");
        if (time_start != NULL) {
            while (*time_start != '(' && *time_start != '\0') {
                strncat(result, time_start, 1);
                time_start++;
            }
        }

        // Парсим возраст
        char *age_start = strstr(line, "(");
        if (age_start != NULL) {
            strcat(result, "; возраст: ");
            age_start++; // Пропустить скобку
            while (*age_start != ')' && *age_start != '\0') {
                strncat(result, age_start, 1);
                age_start++;
            }
        }

        strcat(result, ";\n• ");  // Добавим перевод строки для нового блока
        line = strtok(NULL, "\n"); // Переходим к следующей строке
    }

    (*env)->ReleaseStringUTFChars(env, input, nativeString);

    // Убираем последнюю запятую и возвращаем строку в Kotlin
    size_t len = strlen(result);
    if (len > 2 && result[len - 2] == ',') {
        result[len - 2] = '\0';  // Удаляем последнюю запятую
    } else if (len > 3 && result[len - 3] == "•") {
        result[len - 3] = '\0';  // Удаляем последний символ "•"
    }
    result[len - 6] ='\0';

// Добавляем точку в конце результата, если в нем есть текст
    if (strlen(result) > 0) {
        strcat(result, ".");
    }
    return (*env)->NewStringUTF(env, result);
}
