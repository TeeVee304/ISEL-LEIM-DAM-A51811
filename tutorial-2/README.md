# Tutorial 2
- **Unidade Curricular:** Desenvolvimento de Aplicações Móveis (DAM)
- **Aluno:** Bruno Pereira (51811)
- **Data:** 12/04/2026
- **URL do Repositório:** https://github.com/brunopereira123456789/ISEL-LEIM-DAM-A51811/tree/main/tutorial-2

---

## 1. Introdução
O presente trabalho insere-se no Tutorial 2 da unidade curricular de Desenvolvimento de Aplicações Móveis (DAM). O objetivo consiste no aprofundamento da linguagem Kotlin e no desenvolvimento de aplicações para o ecossistema Android.

O tutorial explora conceitos práticos como classes genéricas, *sealed classes*, *higher-order functions* e *operator overloading*. Adicionalmente, o módulo Android engloba o desenvolvimento de arquiteturas MVVM e a ligação a APIs externas.

## 2. Resumo do Sistema
O trabalho encontra-se estruturado da seguinte forma:
1. `kotlin/`: Conjunto de exercícios no IntelliJ IDEA para consolidação da estrutura gramatical base da linguagem. Cobre tópicos sobre coleções genéricas, manipulação estruturada de eventos, cadeias de processamento de dados e operações matemáticas vetorizadas. 
2. `android/`: Integração de duas aplicações móveis ("Cool Weather APP" e "Mission Impossible Possible 2") focadas no uso de arquiteturas orientadas a eventos e modelos de dados, consumindo serviços web para povoar interfaces de visualização em lista.

## 3. Arquitetura e Design
A organização espacial do repositório reflete a divisão das tecnologias:
- `kotlin/`: Projeto enquadrado no IntelliJ IDEA. Todo o código fonte encontra-se na diretoria `src/main/kotlin/`.
- `android/`: Diretoria composta pelos projetos independentes "Cool Weather APP" e "Mission Impossible Possible 2", estabelecidos pelas normas do gradle no Android Studio. Os projetos implementam a arquitetura estrutural MVVM (Model-View-ViewModel) aliada a RecyclerViews.

## 4. Implementação
Os módulos foram segmentados e implementados do seguinte modo:

**Diretoria `kotlin/src/main/kotlin/`:**
- **`cache.kt`**: Estrutura genérica de mapeamento de valores com operações parametrizáveis de inserção, leitura e transformação funcional.
- **`event.kt`**: Modelação hierárquica através de *sealed classes* representando interações padronizadas, processadas sobre funções extensoras (*extension functions*).
- **`pipeline.kt`**: Sistema de encapsulamento em conduta configurado com abstrações transitivas (*lambda*) para manipulação estrita de metadados em série.
- **`vector.kt`**: Representação de dimensões polares submetidas a reescrita diretiva (*operator overloading*) de matrizes nativas subjacentes e métodos de abstração associativa.

**Diretoria `android/`:**
- **`Cool Weather APP/`**: Aplicação voltada para apresentação de resultados meteorológicos servidos via modelo adaptativo e dispostos visualmente perante *layouts* com dependência horizontal e vertical (RecyclerViews).
- **`Mission Impossible Possible 2/`**: Aplicação de exploração em grelha com consumo de respostas de rede no background. Estruturada sobre os mecanismos de instanciamento do Retrofit.

## 5. Testes e Validação
As resoluções submetidas na plataforma Kotlin revelaram cumprimento nas validações internas associadas à própria lógica, testadas localmente via consola sobre as instâncias de JVM. Observou-se a preservação dos domínios em vetor e a triagem sintática do texto em duto de pipeline. As aplicações da vertente móvel foram submetidas em ambiente controlado no emulador do Android Studio, certificando a transição de atividade com Intent e o carregamento responsivo das imagens captadas remotamente.

## 6. Instruções de Utilização
Para executar os programas em Kotlin:
1. Abrir a diretoria `kotlin/` através do IntelliJ IDEA.
2. Navegar para o diretório `src/main/kotlin/`.
3. Compilar e executar pontualmente as funções `main()` presentes no escopo interno dos respetivos ficheiros.

Para arrancar e testar cada aplicação no Android:
1. Inicializar o IDE Android Studio.
2. Abrir individualmente o projeto listado em causa (ex: `android/Cool Weather APP/`).
3. Aguardar a indexação do projeto ao gradle e importação de dependências manifestas.
4. Conduzir o comando de inicialização com emulador capacitado instalado em sistema virtual ou recurso de hardware externo.

---
# Engenharia de Software Assistida por IA

## 7. Estratégia de Prompting
As iterações incidiram sobretudo em dúvidas explícitas no contorno prático e de arquitetura do desenvolvimento em Android. Empregaram-se perguntas restritas para desmistificar parâmetros da especificação do Retrofit e sobre a correta disposição da notação das rotinas associadas no ambiente Kotlin. No que à vertente do IntelliJ IDEA concerne, foram requeridas definições conceptuais acerca do efeito exato das *extension functions*.

## 8. Fluxo de Trabalho do Agente Autónomo
As ferramentas sugeridas pela IA foram determinantes no suporte para a redação formal do *Boilerplate*, agilizando expressamente a constituição das interfaces de adaptadores (RecyclerView.Adapters) vitais a ambas as aplicações em Android e na depuração célere de conflitos de mapeamento das diretrizes GSON sob respostas em API.

## 9. Verificação de Artefactos Gerados por IA
O código formulado via IA não figurou sem intervenção direta preventiva. A validade da sua inclusão pautou por exames metalinguísticos contrapostas à literatura e com a verificação de execuções controladas na consola ou via dispositivo Android emulacional, assegurando que o consumo de recursos mantivesse as balizas performáticas delimitadas sem quebra abrupta na experiência de utilizador.

## 10. Contribuição Humana vs IA
| Componente | Desenvolvimento |
| :--- | :--- |
| `cache.kt` (Cache Genérica) | Humano |
| `event.kt` (Sistema de Eventos) | Humano |
| `pipeline.kt` (Sistema Modular em Pipeline) | Humano |
| `vector.kt` (*Operator Overloading* em Vetores) | Humano |
| Cool Weather APP | Assistido por IA |
| Mission Impossible Possible 2 | Assistido por IA |

## 11. Uso Ético e Responsável
O trabalho documenta compromisso para com a prática leal em ambiente escolar no que tange ao recurso de artefactos em IA. Em nenhuma instância houve cedência ou supressão do pensamento crítico individual. Todas as deliberações técnicas foram interiorizadas pelo aluno.

---
# Processo de Desenvolvimento

## 12. Controlo de Versão e Histórico de Commits
Os artefactos originados enquadram-se via controlo do Git, ilustrando iterativamente a progressão temporal desde a exploração estrita na linguagem sob JVM ao seu entrosamento com ferramentas do ecossistema Android e construção gradativa da dependência no manifest das aplicações e ficheiros `.gradle`.

## 13. Dificuldades e Aprendizagens
A abordagem ao exercício apresentou oportunidades concisas de aprofundamento com os seguintes fatores principais em evidência:
- Identificação da particularidade concetual ao nível semântico e as devidas vantagens em recorrer às classes fechadas (`sealed class`).
- Organização e gestão estrita de permissões de Internet com asserções assíncronas do Retrofit no enquadramento e manutenção do estado pela ViewModel em Android.

## 14. Melhorias Futuras
No ponto de desenvolvimento exposto, prevê-se expandir cenários nos mecanismos Android à mercê da documentação declarativa do *Jetpack Compose* com injeções controladas usando o *Hilt* para mitigar dependências no `MainActivity`. Adicionalmente as aplicações estatais estariam alinhadas aos padrões recomendáveis se suportassem instâncias da *Room API* para leitura persistente perante falhas temporais nos servidores estáticos.

## 15. Declaração de Utilização de IA
Reporta-se o uso estagnado da Inteligência Artificial em moldes analíticos e correcionais para aceleração restrita do código final sem sobreposição de autorias. O conhecimento e total gerência do processo encontra-se assegurado e refletido no âmbito desta disciplina de Desenvolvimento de Aplicações Móveis.
