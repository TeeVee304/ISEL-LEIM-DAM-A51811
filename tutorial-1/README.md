# Tutorial 1 - Hello Kotlin. Hello Android World!
- **Unidade Curricular:** Desenvolvimento de Aplicações Móveis (DAM)
- **Aluno:** Bruno Pereira (51811)
- **Data:** 08/03/2026
- **URL do Repositório:** https://github.com/brunopereira123456789/ISEL-LEIM-DAM-A51811/tree/main/tutorial-1

---

## 1. Introdução
O presente trabalho enquadra-se no Tutorial 1 de Desenvolvimento de Aplicações Móveis (DAM). O objetivo principal compreende a familiarização com a linguagem de programação Kotlin, bem como na introdução ao desenvolvimento de aplicações para Android. 

O tutorial consolida conceitos básicos da linguagem através de exercícios de aplicação matemática e sintática, progredindo posteriormente para o ecossistema e interface gráfica Android.

## 2. Resumo do Sistema
O trabalho foi desenvolvido em duas frentes de trabalho:
1. Pequenos programas executáveis, desenvolvidos em Kotlin, que exploram estruturas de dados (arrays e listas), cálculos aritméticos e lógicos, sequências (`generateSequence`), e princípios de Programação Orientada a Objetos com a criação e gestão de um sistema de uma biblioteca.
2. Três aplicações móveis focadas na manipulação de eventos, estruturação de componentes em vistas e análise de propriedades.

## 3. Arquitetura e Design
O repositório está subdividido em duas diretorias distintas, refletindo a estrutura supracitada:
- `kotlin/`: Projeto desenvolvido em IntelliJ IDEA, seguindo a hierarquia default (`src/main/kotlin/dam/`).
- `android/`: Diretoria dedicada a instâncias separadas de projetos Android ("Hello World", "System Info", e "Weather Buddy"). A estrutura interna segue o protótipo comum imposto pelo Android Studio e os seus pacotes de gestão.

## 4. Implementação
Os módulos encontram-se estruturados do seguinte modo:

**Diretoria `kotlin/`:**
- **`dam/exer_1/`**: Centra-se na instanciação e mapeamento posicional de arrays (e listas indiretas) iterados via funções lambda.
- **`dam/exer_2/`**: Implementação de uma calculadora com leitura contínua do input por meio do `when`.
- **`dam/exer_3/`**: Simulação do amortecimento de ressaltos de uma bola através da limitação recursiva sobre uma *collection* com tipo derivado de `.takeWhile()` para prevenir saturação dos resultados numéricos.
- **`dam/exer_vl/`**: Lógica de gestão de uma biblioteca (`Library`), fornecendo os processos transacionais de inserção de coleções, rotinas de validações logísticas de devolução, interceção recursiva do percurso e esgotamento do limite nas tiragens dos exemplares.

**Diretoria `android/`:**
- Acondiciona isoladamente as diretorias do projeto `Hello World`, `System Info`, no qual se explora a leitura das informações de hardware, e `Weather Buddy`.

## 5. Testing and Validation
Os exercícios em Kotlin foram validados localmente através da execução no IntelliJ IDEA e verificação dos resultados na consola. Na calculadora (`exer_2`), foram testadas operações matemáticas inválidas como a divisão por zero para garantir o correto funcionamento das exceções. Na aplicação de gestão da biblioteca (`exer_vl`), verificou-se com sucesso o limite de cópias disponíveis aquando da requisição excessiva. Por fim, as três aplicações móveis foram testadas através do emulador integrado no Android Studio, confirmando o carregamento apropriado da interface e a correta resposta aos eventos em cada um dos ecrãs explorados.

## 6. Usage Instructions
Para executar os exercícios enquadrados na diretoria `kotlin/`:
1. Abrir a diretoria `kotlin/` através do IntelliJ IDEA.
2. Navegar para a base de fontes no diretório `src/main/kotlin/dam/`.
3. Executar o ficheiro `.kt` contendo a função principal `main()` respeitando cada exercício respetivo. Relativamente ao `exer_2` será ativada a partilha do canal Standard Input para interação de cálculo.

Para arrancar e testar cada um dos protótipos em `android/`:
1. Inicializar a aplicação Android Studio.
2. Selecionar explicitamente o método de abertura focado unicamente no projeto unitário em questão (ex: `android/System Info/`).
3. Facultar o download sincronizado do *gradle* sobre a matriz da plataforma.
4. Conduzir a compilação no ícone *Run* de modo a arrancar no Emulador pretendido ou recurso físico autorizado por USB.

---
# Engenharia de Software Assistida por IA
## 7. Estratégia de Prompting
As interações com as ferramentas de inteligência artificial foram realizadas de forma iterativa com o intuito de esclarecer dúvidas concretas durante o desenvolvimento. Foram efetuadas perguntas curtas para validar a estrutura dos ciclos `do-while` e blocos `try-catch` nos exercícios em Kotlin, e pontualmente para auxiliar na resolução de erros associados ao compilador Gradle no Android Studio.

## 8. Workflow do Agente Autónomo
As ferramentas de IA demonstraram utilidade primária na aceleração da escrita e formatação do código. Desempenharam um papel prático na identificação rápida de problemas de sintaxe e na recomendação de funções de biblioteca próprias do Kotlin (como a criação simplificada de sequências iteráveis), evadindo cenários de erro e bloqueio da aplicação.

## 9. Verificação de Artefactos Gerados por IA
O código sugerido pela IA passou sempre por uma fase de teste local antes da integração. As soluções foram validadas e testadas manualmente tanto no IDE (IntelliJ) como no emulador Android para confirmar a sua correção face aos requisitos. Todos os trechos sofreram os devidos ajustes humanos necessários para corrigir formatações de sintaxe ou dependências inadequadas.

## 10. Contribuição Humana vs IA

| Componente | Desenvolvimento |
| :--- | :--- |
| Exercício 1 - Instanciação de Arrays | Humano |
| Exercício 2 - Calculadora | Humano |
| Exercício 3 - Modelação Sequencial | Humano |
| Exercício VL - Gestão de Biblioteca | Humano |
| Hello World | Assistido por IA |
| System Info | Assistido por IA |
| Weather Buddy | IA |

## 11. Uso Ético e Responsável
A utilização das ferramentas de IA foi feita de forma estritamente consciente, garantindo a compreensão de todo o código sugerido antes da sua aplicação. As soluções geradas passaram por uma análise crítica com o objetivo de proibir a inserção de código incorreto ou fora de contexto. A responsabilidade pelas decisões de design, funcionalidade dos artefactos e autoria direta da solução final manteve-se inteiramente do lado do aluno.

---
# Processo de Desenvolvimento
## 12. Controlo de Versão e Histórico de Commits
O sistema Git foi selecionado para versionar gradualmente as alterações ao longo do desenvolvimento deste guia. Os commits refletem fielmente a evolução do projeto passo a passo, documentando a resolução inicial dos vários algoritmos em consola na subdiretoria `kotlin/` até às sucessivas adições de layouts gráficos e configuração global das aplicações contidas em `android/`.

## 13. Dificuldades e Aprendizagens
As principais dificuldades e desafios encontrados durante a resolução prática focaram-se em três aspetos específicos:
- **Layouts e Constraints (Android):** Adaptação ao posicionamento de componentes e gestão de constrangimentos na construção das interfaces gráficas.
- **API Fetching ("Weather Buddy"):** Compreensão do ciclo de requisições de rede para obter dados da meteorologia no background e apresentá-los no último ecossistema móvel.
- **Bitwise Shifts (Kotlin):** Entendimento concetual das operações lógicas de deslocamento de bits (`shl` e `shr`) no desenvolvimento do construto da calculadora (Exercício 2).

## 14. Melhorias Futuras
Uma extensão que poderia acrescentar riqueza ao trabalho envolveria a migração da base das aplicações em Android para implementações declarativas nativas utilizando o Jetpack Compose em oposição à abordagem enraizada em ficheiros XML. Desta forma seria também vantajoso dotar o programa de um leque operacional fundamentando nas chamadas assíncronas do Kotlin via Coroutines.

---
## 15. Declaração acerca da Utilização de IA
O trabalho de laboratório foi desenvolvido e ocasionalmente corroborado com a consulta de modelos linguísticos primários. Afirma-se inteiramente a responsabilidade do utilizador pelas decisões tomadas na constituição da resolução entregue, e total discernimento sobre todas as mecânicas finais introduzidas submetidas sob a sua alçada no repositório.
