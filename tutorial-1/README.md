# Tutorial 1 - Hello Kotlin. Hello Android World!
**Unidade Curricular:** Desenvolvimento de Aplicações Móveis (DAM)
**Aluno:** Bruno Pereira (51811)
**Data:** 08/03/2026
**URL do Repositório:** https://github.com/brunopereira123456789/ISEL-LEIM-DAM-A51811/tree/main/tutorial-1

---

## 1. Introdução
O presente trabalho enquadra-se no Tutorial 1 de Desenvolvimento de Aplicações Móveis (DAM). O objetivo principal compreende a familiarização com a linguagem de programação Kotlin, bem como na introdução ao desenvolvimento de aplicações para Android. 

O tutorial consolida conceitos básicos da linguagem através de exercícios de aplicação matemática e sintática, progredindo posteriormente para o ecossistema e interface gráfica Android.

## 2. System Overview
A solução desenvolve-se através de duas frentes de trabalho:
- **Exercícios de Kotlin (Consola):** Consiste em pequenos programas executáveis que demonstram o uso de estruturas de dados (arrays e listas), a materialização de uma interface de linha de comandos interativa (REPL) para efetuar cálculos aritméticos e lógicos, o uso de sequências (`generateSequence`) de aplicação livre, e princípios de Programação Orientada a Objetos com a criação e gestão de um sistema de uma biblioteca.
- **Aplicações Android:** Engloba a construção e exploração de três aplicações móveis focado na manipulação de eventos, estruturação de componentes em vistas e análise de propriedades de forma exploratória.

## 3. Architecture and Design
O repositório está subdividido em duas diretorias distintas:
- `kotlin/`: Estruturado enquanto projeto modular em IntelliJ IDEA de acordo com a hierarquia da ferramenta de compilação adotada (`src/main/kotlin/dam/`). Evidenciam-se opções de design suportadas na programação funcional característica do Kotlin (uso de `when` como expressão avaliativa interativa das opções e manipulação direta de funções iteráveis) bem como modelação do domínio físico usando o encapsulamento orientado a objetos onde instâncias especializadas formam as devidas classes (como `DigitalBook` e `PhysicalBook` expandido via abstração).
- `android/`: Diretório dedicado a instâncias separadas de projetos Android ("Hello World", "System Info", e "Weather Buddy"). A estruturação interna segue o protótipo comum imposto pelo Android Studio e os seus pacotes de gestão (`Activities` sobre XML, fragmentos e lógica partilhada de acordo correspondendo ao ciclo de inicialização mobile).

## 4. Implementation
Os módulos encontram-se estruturados do seguinte modo:

**Dentro da diretoria `kotlin/`:**
- **`dam/exer_1/`**: Centra-se na instanciação e mapeamento posicional de arrays (e listas indiretas) iterados via funções lambda.
- **`dam/exer_2/`**: Implementação de uma calculadora REPL com avaliação contínua do input por meio do `when`. Oculta conversão iterada e implementa controlo matemático de restrições sensíveis (como validações de divisão por zero) emitindo as exceções requeridas no formato de controlo `try-catch`.
- **`dam/exer_3/`**: Computação de saltos dinâmicos baseados no amortecimento limitante de ressaltos de uma bola através da limitação recursiva sobre uma *collection* com tipo derivado de `.takeWhile()` para prevenir saturação assintótica dos resultados numéricos.
- **`dam/exer_vl/`**: Lógica de modulação da biblioteca (`Library`), fornecendo os processos transacionais de inserção de coleções, rotinas de validações logísticas de devolução, interceção recursiva do percurso e esgotamento do limite nas tiragens dos exemplares.

**Dentro da diretoria `android/`:**
- Acondiciona isoladamente aos diretórios do projeto `Hello World` e de leitura da extração hardware alocado ao ecletismo da interface em `System Info`, culminando na exploração arquitetural introduzida a navegações mais robustas em `Weather Buddy`.

## 5. Testing and Validation
Os modelos programados com destinação na JVM (em Kotlin) sofreram testes por via de experimentação singular informal no ambiente gráfico IDE atestando as saídas formatadas. Procedeu-se adicionalmente à aferição intencional de divisões de erros zero e de entradas inválidas ao parser na REPL do `exer_2` garantindo contenção pela devida ramificação do `Exception`. Testou-se do mesmo modo a indisponibilidade materializada das unidades em limite da biblioteca no `exer_vl` validando as respostas do domínio. Oarante formal dos executáveis sobre Android valeu-se do ecrã virtual do Emulador interno das ferramentas de engenharia, verificando se cada transição e formatação respondem corretamente aos comportamentos exigidos. 

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
# Autonomous Software Engineering Sections - only for [AC OK, AI OK] sections
## 7. Prompting Strategy
As sessões de conversação (*prompting*) decorreram com preceitos de intermitência direcionada validando progressivamente, de modo compartimentado, as construções idiomáticas em Kotlin. Foram usadas queries incisivas para clarificar a abordagem ao input via `do-while` aliado ao controlo imperativo do `try-catch`, estendendo por fim esta mecânica orientativa aquando das incertezas procedimentais inerentes à dependência do compilador de Gradle nas ferramentas móveis durante as integrações android.

## 8. Autonomous Agent Workflow
O agente AI manifesta a sua incorporação na aceleração inicial da refatoração descrita de métodos, elucidando potenciais fugas aos bloqueios recursivos bem como proporcionando vias alternativas, mas idiomáticas associadas ao Kotlin moderno. Exibiu também de forma indireta ações determinantes de sintaxe auxiliando as avaliações rápidas para a formulação funcional declarativa em prol das convencionais, simplificando as etapas preliminares ou interceções dos bloqueadores de IDE.

## 9. Verification of AI-Generated Artifacts
Toda e qualquer solução provida e integrada sobreviveu a testagem empírica, linha a linha e sendo estritamente revista em execução direta na JVM ou Emulador face ao contexto e output ambicionado da lógica definida nas normas submetidas no escopo inicial. Ajustou-se de forma manual todo o enquadramento de formato irregular, dependências estáticas não compatíveis com as diretivas e discrepâncias sobre bibliotecas.

## 10. Human vs AI Contribution

| Componente | Contribuição Humana | Contribuição AI |
| :--- | :--- | :--- |
| **Estrutura de Diretórios e Escopo** | Determinação ativa da divisão modular entre ambientes IntelliJ (Kotlin) e Android Studio. | Intervenção suprimida (Nula). |
| **Exercício 2 (REPL Iterativo)** | Design da limitação sequencial de divisão de instâncias lógicas e contenção global imperativa perante divisão por zero na rotina principal matemática. | Refinamento para parsing, delineação robusta da leitura de comandos pelo construto `trim()` e encurtamento interativo dos matches no percurso. |
| **Algoritmia Sequencial (Exercício 3)** | Encadeamento e delimitação estrutural face à limitação natural pretendida com adaptação formatada do texto resultante a métricas base de arredondamentos (`.2f`). | Recomendação assertiva sobre blocos dinâmicos usando `generateSequence`, em favor da formatação cíclica não declarativa mitigando recursões. |
| **POO da Biblioteca (Exercício VL)** | Mapeamento das estruturas internas e do encapsulamento final. Delimitação das métricas de bloqueios logísticos (`PhysicalBook` esgotável, por exemplo). | Refatorização simplificatória de parâmetros internos (`data class` e correlacionados eventuais associadas à tipagem no `Library`). |
| **Sistema Android Básicos** | Configuração inicial, mapeamento dos views ao respetivo control Kotlin e inicialização interativa dos comportamentos das activities e layouts nos ecrãs móveis exploráveis. | Ajuste pontual e aconselhamentos analíticos à arquitetura sobre bibliotecas dependentes (noções dos scripts `.gradle`). |

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
