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
# Autonomous Software Engineering
## 7. Estratégia de Prompting
As interações com as ferramentas de inteligência artificial foram realizadas de forma iterativa com o intuito de esclarecer dúvidas concretas durante o desenvolvimento. Foram efetuadas perguntas curtas para validar a estrutura dos ciclos `do-while` e blocos `try-catch` nos exercícios em Kotlin, e pontualmente para auxiliar na resolução de erros associados ao compilador Gradle no Android Studio.

## 8. Workflow do Agente Autónomo
As ferramentas de IA demonstraram utilidade primária na aceleração da escrita e formatação do código. Desempenharam um papel prático na identificação rápida de problemas de sintaxe e na recomendação de funções de biblioteca próprias do Kotlin (como a criação simplificada de sequências iteráveis), evadindo cenários de erro e bloqueio da aplicação.

## 9. Verificação de Artefactos Gerados por IA
O código sugerido pela IA passou sempre por uma fase de teste local antes da integração. As soluções foram validadas e testadas manualmente tanto no IDE (IntelliJ) como no emulador Android para confirmar a sua correção face aos requisitos. Todos os trechos sofreram os devidos ajustes humanos necessários para corrigir formatações de sintaxe ou dependências inadequadas.

## 10. Contribuição Humana vs IA

| Componente | Desenvolvimento |
| :--- | :--- |
| Estrutura de Diretórios e Escopo | Humano |
| Exercício 2 (Calculadora REPL) | Híbrido |
| Algoritmia Sequencial (Exercício 3) | Híbrido |
| POO da Biblioteca (Exercício VL) | Humano |
| Aplicações Básicas do Android | Híbrido |

## 11. Ethical and Responsible Use
Foram cumpridos estritamente os imperativos éticos recomendados, em particular a compreensão integral dos trechos sugeridos no intuito de isolar falsas soluções e adulteração desprovidas de contexto. Assegura-se de igual modo, assunção de compromisso pela propriedade perante cada solução injetada pelo modelo por meio do crivo formativo de adequação técnica sem isenção de autoria académica para cada exercício validado individualmente.

---
# Development Process
## 12. Version Control and Commit History
Procedimentos apoiados metodologicamente em Git de modo a proporcionar *snapshots* seccionados no avanço de complexidade sobre ambas as fronteiras linguísticas propostas da diretoria mãe. Desde protótipos singulares de console como materializados nos registos remanescentes do Repositório até interações de adição sucessivas da compilação e dos XML nos ecossistemas de projeto base móveis em Android.

## 13. Difficulties and Lessons Learned
Destacou-se em especial as incongruências subjacentes aos paradigmas declarativos na mutação de dados não triviais que marcam o ecossistema e abstração idiomática presentes na linguagem face a alternativas puramente orientadas. Superou-se em igual destaque as barreiras comuns relativas à curva gradual de introdução perante os manifestos limitadores de configurações, ficheiros complexos e assimetria do IDE do percurso focado nas aplicações de Android. Fomentou o cimentar crucial da interpretação rápida e reestruturação para depuração.

## 14. Future Improvements
Potenciais extensões alavancam o reajuste perante abordagens modernas focadas noutras bibliotecas ricas padronizadas introduzidas na linguagem, no uso do enquadramento de *flows*, rotinas assíncronas dedicadas na verificação interativa e eventual expansão do Android num domínio integral em migração generalista das vistas gráficas de XML para os declarativos de interfaces.

---
## 15. AI Usage Disclosure (Mandatory)
Na execução destas preposições exploratórias de introdução pontual valeu-se da utilização estrita dos Large Language Models, limitadamente atuando de caráter complementar com intervenção pontual. Envolveu abordagens pragmáticas de elucidações declarativas e desambiguações de documentação formatada pela comunidade para as funções, mantendo e atestando integralmente por responsabilidade central todo o planeamento de controlo estrutural presente nos algoritmos originados.
