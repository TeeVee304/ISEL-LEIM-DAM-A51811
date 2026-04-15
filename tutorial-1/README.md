# Assignment 1 — Tutorial 1 - Hello Kotlin. Hello Android World!

Course: DAM (Desenvolvimento de Aplicações Móveis)
Student(s): Bruno
Date: 15/04/2026
Repository URL: https://github.com/TeeVee304/ISEL-LEIM-DAM-A51811

---

## 1. Introduction
O objetivo deste primeiro trabalho prático (Tutorial 1) é introduzir o ecossistema de desenvolvimento Android, focado na linguagem Kotlin e no Android Studio, estabelecendo as bases necessárias para o desenvolvimento de aplicações móveis na disciplina de DAM. O projeto está dividido em duas partes fundamentais: exercícios introdutórios de consolidação em Kotlin e o desenvolvimento das três primeiras aplicações móveis para a plataforma Android (Hello World, System Info, Weather Buddy).

## 2. System Overview
O repositório apresenta as seguintes componentes principais:
1. **Exercícios em Kotlin**: Projetos desenvolvidos no IntelliJ IDEA para explorar sintaxe, estruturas de dados, e peculiaridades da linguagem Kotlin em cenários baseados na JVM.
2. **Aplicações Android**:
    *   **Hello World**: A aplicação mais fundamental, concebida para compreender a estrutura básica de um projeto Android e o ciclo de vida de uma Atividade (Activity).
    *   **System Info**: Uma aplicação utilitária simples que ilustra como interagir com o sistema operativo para extrair informações do dispositivo.
    *   **Weather Buddy**: A mais complexa das três, que integra fragmentos (Fragments), navegação (Navigation Component) e vistas xml.

## 3. Architecture and Design
A estrutura do repositório reflete as duas áreas de foco exploradas:
*   `kotlin/`: Contém um projeto Maven/Gradle cujos ficheiros se encontram na subdiretoria `src/main/kotlin/dam/` divididos por pacotes correspondentes aos vários exercícios abordados (`exer_1/`, `exer_2/`, `exer_3/`, `exer_vl/`). A decisão por uma estrutura canónica facilita a integração com IDEs.
*   `android/`: Organiza as aplicações Android desenvolvidas independentemente em subdiretorias:
    *   `android/Hello World/`
    *   `android/System Info/`
    *   `android/Weather Buddy/` 
O design das aplicações Android segue, neste estádio inicial, o padrão convencional (MVC/MVP simplificado associado em exclusivo a Activities). Na Weather Buddy, começou-se a explorar a navegação entre ecrãs com o Jetpack Navigation Component.

## 4. Implementation
Os módulos implementados representam as abordagens adequadas para iniciar o estudo de Kotlin:
*   **Exercícios Kotlin (`kotlin/`)**: Implementam lógica imperativa usando tipos nativos, classes, funções de extensão e hierarquias simples em Kotlin.
*   **Aplicações Android (`android/`)**:
    *   Os projetos utilizam Views e Layouts XML padrão do Android (ex: ConstraintLayout, LinearLayout). A lógica de negócio essencial para associar as View ao Modelo é mantida na MainActivity (e em Fragments, no caso da aplicação Weather Buddy). A aplicação Weather Buddy destaca o uso do `NavHostFragment`.

## 5. Testing and Validation
Nesta fase introdutória, não existem Testes Unitários formais ou de Interface Gráfica automatizados (como Expresso ou JUnit clássico) parametrizados.
A validação consistiu em **Testes Manuais** no emulador do Android Studio (Pixel 6) para a componente Android e execução isolada no IDE para a componente Kotlin:
*   Execução bem-sucedida de ficheiros da componente de Kotlin no emulador da JVM (IntelliJ).
*   Visualização da correta tradução de layout XML no emulador para os 3 projetos Android.
*   Navegação sem *crashes* na aplicação "Weather Buddy" entre os fragments existentes. 

## 6. Usage Instructions
### Para a componente em Kotlin (`kotlin/`):
1.  Importar o projeto que se encontra na diretoria raiz `kotlin` utilizando o **IntelliJ IDEA**. O ficheiro raiz necessário para a configuração está preparado (`pom.xml`).
2.  Navegar até à pasta `./src/main/kotlin/dam/` e executar os diversos exercícios de Kotlin individualmente a partir do IDE.

### Para a componente Android (`android/`):
1.  Abrir o **Android Studio**.
2.  Selecionar a opção **"Open an existing Android Studio project"** e navegar até à diretoria específica da aplicação correspondente a cada um dos projetos (`Hello World/`, `System Info/`, ou `Weather Buddy/`).
3.  Permitir que o Gradle faça a sincronização total das dependências, atualizando a configuração para a JVM conforme a versão especificada na build do Gradle.
4.  Selecionar um emulador (ex: API 34 ou semelhante, criado antecipadamente no Device Manager) ou um dispositivo físico que se encontre com o modo desenvolvedor ativado.
5.  Pressionar **"Run"** (`Shift + F10`).

---

# Autonomous Software Engineering Sections
## 7. Prompting Strategy
As estratégias de *prompting* utilizadas focaram-se na explicitação extensiva e clara das propriedades desejadas na arquitetura e nas linguagens base. Os prompts forneceram um contexto estruturado para refatorização do Gradle, migração para navegação com Fragments, ou a resolução de falhas técnicas na ligação entre layout (XML) e código funcional. 
*Exemplo representativo*: Ao solucionar um problema de integração no "Weather Buddy", forneceu-se o `nav_graph.xml` e requereu-se ao agente que identificasse o problema na implementação da navegação, mantendo o estrito rigor nos nomes dados no layout xml.

## 8. Autonomous Agent Workflow
O desenvolvimento contou com o auxílio fundamental de um agente de IA de engenharia de software para orquestrar o processo de planeamento e as correções pontuais em *debugging*. 
A Inteligência Artificial procedeu de forma autónoma à pesquisa pelo workspace, propôs arquiteturas de base no caso da aplicação "Weather Buddy", analisou iterativamente e sugeriu implementações sobre erros complexos baseados na configuração correta do Gradle para uma versão superior.

## 9. Verification of AI-Generated Artifacts
Todos os artefactos, planos de implementação e adições em código resultantes de intervenções do agente foram alvo de verificação direta contínua e cruzamento, garantindo que respondiam perfeitamente aos pressupostos originais em `kotlin` ou no `build.gradle` das aplicações envolvidas. A validação ocorreu em simultâneo com a execução compilada no IntelliJ ou no Emulador do Android Studio. 

## 10. Human vs AI Contribution
*   **Contribuição Humana**: Conceção das especificações originais da interface; Planeamento de alto-nível dos *use cases* a concretizar em Kotlin e testagem direta no terreno da eficiência. Organização de todas as referências no repositório.
*   **Contribuição IA**: Apoio no diagnóstico das resoluções de problemas (notoriamente `binding` no Gradle, navegação fragmentada, sintaxe XML), redação extensiva de documentação sobre configuração da Base de Dados na fase anterior, criação de propostas de refatorização rápida, ou a conversão de modelos (ex: diagramas PlantUML para DDL). 

## 11. Ethical and Responsible Use
Dada a natureza das intervenções, as respostas foram monitorizadas de modo a assegurar que o desenvolvimento ocorria sem a criação ou importação de quaisquer licenças externas e dependências duvidosas/incompatíveis. O agente provou utilidade restrita e direcionada aos comandos efetuados sem extrapolações perigosas.

---

# Development Process
## 12. Version Control and Commit History
Embora um histórico de commits detalhado seja a prática comum, durante o desenvolvimento dos pequenos projetos que se concretizaram neste tutorial (versão essencial) o processo operou mais num modelo de prototipagem expedita com a validação final. A estruturação definitiva ficou em linha para garantir total integração das tarefas.

## 13. Difficulties and Lessons Learned
As principais dificuldades encontradas envolveram os desafios de configuração contínua por discrepâncias de versões das ferramentas entre a definição das bibliotecas essenciais e a imposição local do IDE. Os processos de resolução do problema do Jetpack Data/View Binding permitiram clarificar que frequentemente as soluções requerem um trabalho iterativo de limpeza no Gradle Cache para funcionar como deviam. Obteve-se um maior conhecimento geral do ecosssistema de Fragmentos no Android (vs ciclo de vida original de cada *Activity* isolada). 

## 14. Future Improvements
*   Implementação de Testes Automáticos para robustecer o processo global;
*   Integração do Jetpack Compose em substituição do modelo de XML tradicional.
*   Nas aplicações como o "Weather Buddy", estabelecer uma ligação verdadeira com a Web API do tempo baseada em ficheiros JSON reais manipulados via base de dados assíncrona.

---

## 15. AI Usage Disclosure (Mandatory)
Nesta UC e no tutorial atual foi feita utilização profunda e transversal da Inteligência Artificial do Google Gemini Agent Integrada no IDE (e histórico registado da interação em conversações via API - Antigravity Agent Model 3.1 Pro High). Utilizou-se a IA para as fases essenciais do processo: Definição, Depuração e Documentação, bem como apoio ao levantamento e explanação da arquitetura original em C#.
Eu, o estudante, permaneço sempre responsável por todo o processo e por todo o conteúdo incluído neste repositório bem como da lógica arquitetural aprovada.
