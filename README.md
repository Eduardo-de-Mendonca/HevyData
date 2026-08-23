# Objetivo
Este programa lê os dados exportados em CSV pelo Hevy, um sistema feito para acompanhar treinos de musculação. Com base nos dados, o programa escreve um resumo indicando quais os exercícios da rotina atual de treinos, bem como o peso e as quantidades de repetições de cada um.

# Como usar
Exporte seus dados do Hevy no formato CSV. Um exemplo de arquivo exportado dessa maneira está em `usage_example\workout_data.csv`.

Depois, execute o comando:
`.\gradlew run --args="path\absoluto\até\o\arquivo.csv"`

O programa gerará o arquivo de saída na mesma pasta do arquivo de entrada. Por exemplo, o arquivo `usage_example\workout_data_output.txt` foi gerado dessa maneira.