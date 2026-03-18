# Estrutura da Aplicação (`app/`)

## Ficheiros na Raiz
* `build.gradle.kts`: O script de compilação Gradle específico para o módulo da aplicação, definindo as suas dependências, plugins e configurações do Android.
* `proguard-rules.pro`: Contém regras para personalizar a forma como o ProGuard/R8 reduz e ofusca o código da aplicação para "release builds" (versões de lançamento).

## Código Fonte (`src/main/`)
* `AndroidManifest.xml`: O ficheiro de configuração essencial que define as permissões da aplicação, os componentes e os requisitos de hardware para o sistema operativo Android.

### Código Kotlin (`src/main/java/dam_a51811/weatherbuddy/`)
* **`MainActivity.kt`**: A "Activity" (atividade) principal que serve de ponto de entrada e aloja a interface de utilizador (UI) e o gráfico de navegação.
* **`WeatherBuddyApp.kt`**: A classe `Application` personalizada, utilizada principalmente para inicializar funcionalidades globais da aplicação, como a injeção de dependências Hilt.
* **`data/`**: A camada responsável por obter dados de APIs externas ou de bases de dados locais.
  * `remote/OpenWeatherApi.kt`: Uma interface Retrofit que define os pedidos HTTP exatos a fazer à API do OpenWeatherMap.
  * `remote/WeatherDto.kt`: Objetos de Transferência de Dados (DTOs) que mapeiam rigorosamente a estrutura JSON devolvida pela API de meteorologia.
  * `repository/WeatherRepositoryImpl.kt`: A implementação concreta que obtém os dados da API e os converte em modelos de domínio limpos.
* **`di/`**: A camada de Injeção de Dependências.
  * `AppModule.kt`: Um módulo Dagger-Hilt que fornece as instruções sobre como instanciar objetos como o cliente da API e os repositórios.
* **`domain/`**: A camada central da lógica de negócio, que permanece completamente independente da interface de utilizador do Android e de frameworks externos.
  * `model/WeatherInfo.kt`: O modelo limpo, em Kotlin puro, que representa os dados meteorológicos utilizados pela lógica e interface da aplicação.
  * `repository/WeatherRepository.kt`: A interface que define as operações de dados exigidas pelo domínio, abstraindo os detalhes de implementação.
* **`presentation/`**: A camada da Interface de Utilizador, seguindo a arquitetura MVVM para separar a interface da lógica.
  * `map/MapFragment.kt`: O ecrã da interface responsável por apresentar o mapa interativo do Google Maps ao utilizador.
  * `map/MapViewModel.kt`: Gere o estado e a lógica do ecrã do mapa, sobrevivendo a alterações de configuração, como as rotações de ecrã.
  * `weather/WeatherFragment.kt`: O ecrã da interface responsável por apresentar os detalhes meteorológicos obtidos para a localização selecionada.
  * `weather/WeatherViewModel.kt`: Gere os dados e o estado reativo da interface (A carregar, Sucesso, Erro) para o ecrã de meteorologia.

## Recursos Android (`src/main/res/`)
* **`drawable/`**: Contém gráficos vetoriais, "drawables" em XML e imagens PNG personalizadas, utilizados em toda a aplicação.
* **`layout/`**: Contém os ficheiros XML (`activity_main.xml`, `fragment_map.xml`, `fragment_weather.xml`) que constroem a estrutura visual dos ecrãs.
* **`mipmap/`**: Contém os ícones de lançamento da aplicação em várias resoluções para suportar diferentes densidades de ecrã dos dispositivos.
* **`navigation/`**:
  * `nav_graph.xml`: Define todos os fluxos de navegação e a passagem segura de argumentos entre "fragments" através do Jetpack Navigation Component.
* **`values/`**: Contém variáveis de estilo e texto para manter a consistência em toda a aplicação.
  * `colors.xml`: Define a paleta de cores base utilizada pelo tema Material da aplicação.
  * `strings.xml`: Centraliza todo o texto escrito na aplicação para evitar a codificação rígida ("hard-coding") e facilitar futuras traduções.
  * `themes.xml`: Define as regras e atributos de estilo do Material Design 3 tanto para o modo claro, como para o modo escuro.
* **`xml/`**:
  * `backup_rules.xml` & `data_extraction_rules.xml`: Ficheiros de configuração que ditam como o sistema automático de cópias de segurança na nuvem do Android gere os dados da aplicação.

## Testes (`src/androidTest/` & `src/test/`)
* **`src/androidTest/.../ExampleInstrumentedTest.kt`**: Testes de interface de utilizador (UI) e instrumentação que requerem um dispositivo Android real ou um emulador para testar as interações com a framework.
* **`src/test/.../ExampleUnitTest.kt`**: Testes unitários locais que são executados rapidamente na máquina virtual Java (JVM) local porque não dependem da framework do Android.
