#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <regex.h>

#define INPUT_FILE "output.html"
#define OUTPUT_FILE "validated_output.html"

void validate_html(const char *input_path, const char *output_path) {
    FILE *input = fopen(input_path, "r");
    FILE *output = fopen(output_path, "w");

    if (!input || !output) {
        printf("Error opening files!\n");
        return;
    }

    char line[1024];
    regex_t regex;
    int reti = regcomp(&regex, "<h1>.*</h1>|<p>.*</p>", REG_EXTENDED);

    if (reti) {
        printf("Regex compilation error!\n");
        return;
    }

    while (fgets(line, sizeof(line), input)) {
        if (!regexec(&regex, line, 0, NULL, 0)) {
            fprintf(output, "%s", line);
        }
    }

    regfree(&regex);
    fclose(input);
    fclose(output);
    printf("Validation completed! Output saved to %s\n", OUTPUT_FILE);
}

int main() {
    validate_html(INPUT_FILE, OUTPUT_FILE);
    return 0;
}
