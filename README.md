# TpGit_IngyCal

Trabajo Práctico Integrador de **Ingeniería y Calidad de Software**
(UTN - Facultad Regional Santa Fe).
Tema: *Sistemas de Gestión de la Configuración* con Git, GitHub y Gitflow.

El repositorio contiene un software Java mínimo (`clase.java`, un modelo
de persona con nombre, apellido, edad, documento y estado civil). El
código no es el objetivo del TP: sirve para practicar el ciclo completo
de gestión de la configuración (ramas, Pull Requests, releases, hotfixes,
CI, revisión de código y documentación).

---

## Tabla de contenidos

1. [Integrantes](#integrantes)
2. [Requisitos y ejecución](#requisitos-y-ejecución)
3. [Estructura del repositorio](#estructura-del-repositorio)
4. [Flujo de trabajo: Gitflow](#flujo-de-trabajo-gitflow)
5. [Convenciones](#convenciones)
6. [Integración continua y revisión](#integración-continua-y-revisión)
7. [Historial de versiones](#historial-de-versiones)
8. [Respuestas a la consigna](#respuestas-a-la-consigna)

---

## Integrantes

| Integrante             | Usuario GitHub         |
|------------------------|------------------------|
| Agustín García         | @agustingarciaUTN      |
| Marcos Ignacio Gianni  | @MarcosIgnacioGianni   |
| Matías Decolatti       | @MatiDecoUTN           |

Los tres figuran como responsables de revisión en `.github/CODEOWNERS`.

## Requisitos y ejecución

- JDK 17 o superior.
- (Opcional) IntelliJ IDEA. Los archivos del IDE no se versionan.

```bash
git clone https://github.com/agustingarciaUTN/TpGit_IngyCal.git
cd TpGit_IngyCal
javac clase.java
```

## Estructura del repositorio

```
.
├── .github/
│   ├── CODEOWNERS                 # responsables de revisión
│   ├── pull_request_template.md   # plantilla obligatoria de PR
│   └── workflows/ci.yml           # pipeline de CI
├── .gitignore                     # archivos locales excluidos
├── clase.java                     # código fuente
└── README.md
```

## Flujo de trabajo: Gitflow

| Rama         | Propósito                                        | Nace de   | Se integra en        |
|--------------|--------------------------------------------------|-----------|----------------------|
| `main`       | Código en producción. Cada merge lleva un tag.   | -         | -                    |
| `develop`    | Integración de lo que entra en la próxima versión | `main`    | -                    |
| `feature/*`  | Nueva funcionalidad                              | `develop` | `develop`            |
| `release/*`  | Estabilizar una versión antes de producción      | `develop` | `main` y `develop`   |
| `hotfix/*`   | Corrección urgente sobre producción              | `main`    | `main` y `develop`   |

Ramas usadas en este TP: `feature/clase`, `feature/setEstudiante`,
`release/1.0.0` y `hotfix/1.0.1-error-envio`.

## Convenciones

**Commits**
- Título conciso, en imperativo, de 50 caracteres como máximo.
- Línea en blanco y cuerpo que explica *qué* y *por qué*, con líneas de
  72 caracteres como máximo.

**Versionado:** [SemVer](https://semver.org/lang/es/) `MAYOR.MENOR.PARCHE`.
Las releases incrementan MENOR o MAYOR y los hotfixes incrementan PARCHE.

**Pull Requests:** todo cambio entra por PR, completa la plantilla y
necesita la aprobación de al menos un CODEOWNER.

## Integración continua y revisión

- `.github/workflows/ci.yml` se ejecuta en cada Pull Request y en cada
  push a `main` o `develop`.
- `.github/CODEOWNERS` pide automáticamente la revisión de los
  responsables al abrir un PR.

## Historial de versiones

| Versión  | Descripción                                                    |
|----------|----------------------------------------------------------------|
| `v1.0.0` | Primera release: modelo con estado civil y `setEstudiante`.    |
| `v1.0.1` | Hotfix: se eliminan los campos "Estudiante", que hacían fallar el envío de una request HTTP. |
| `v1.1.0` | Nueva funcionalidad: cálculo de la edad (`calcularEdad`). La altura del sujeto se incorporó y luego se revirtió. |
| `v1.1.1` | Documentación: README, `.gitignore` y plantilla de pull request. |

---

## Respuestas a la consigna

### 7.b ¿Cómo evitamos subir cambios de configuraciones locales?

Con un archivo **`.gitignore`** en la raíz. Allí se listan los patrones
que Git no debe seguir: carpetas del IDE (`.idea/`, `*.iml`, `.vscode/`),
compilados (`*.class`, `target/`, `out/`), variables de entorno y
secretos (`.env`) y archivos del sistema operativo (`.DS_Store`,
`Thumbs.db`).

`.gitignore` solo afecta a los archivos que **no están versionados**. Si
un archivo ya estaba en el índice, hay que dejar de seguirlo sin borrarlo
del disco:

```bash
git rm -r --cached .idea TpGit_IngyCal.iml
git commit
```

Para excepciones que valen solo en una máquina y no deben compartirse
con el equipo existe `.git/info/exclude`. Para un archivo versionado que
se modifica localmente (por ejemplo un `application.properties`), la
práctica recomendada es versionar una plantilla (`.env.example`) e
ignorar la copia real.

### 7.c / 7.d Preparar el Release 1

```bash
git checkout develop
git checkout -b release/1.0.0
# solo ajustes de estabilización: versión, correcciones menores, docs
git commit -am "Version 1.0.0 lista para produccion"
git push -u origin release/1.0.0
```

En una rama `release/*` no se agrega funcionalidad nueva, solo lo
necesario para dejar la versión lista para producción.

### 7.e Llevar Release 1 a producción siguiendo Gitflow

La rama de release se integra en **`main`** (producción), se etiqueta y
**también vuelve a `develop`**, para que los ajustes hechos durante la
estabilización no se pierdan en la próxima versión:

```bash
git checkout main
git merge --no-ff release/1.0.0
git tag -a v1.0.0 -m "Release 1.0.0"
git checkout develop
git merge --no-ff release/1.0.0
git push origin main develop --tags
git branch -d release/1.0.0
```

`--no-ff` conserva un commit de merge que deja claro en el historial
dónde empezó y terminó la release.

### 7.f / 7.g Corregir un error en producción (hotfix)

Un error en producción **no** se corrige en `develop`, porque ahí puede
haber funcionalidad sin terminar. Se crea una rama `hotfix/*` desde
`main`, se corrige y se integra en `main` **y** en `develop`:

```bash
git checkout main
git checkout -b hotfix/1.0.1-error-envio
# corrección
git commit -am "Fix eliminación campos 'Estudiante'"

git checkout main
git merge --no-ff hotfix/1.0.1-error-envio
git tag -a v1.0.1 -m "Hotfix 1.0.1"

git checkout develop
git merge --no-ff hotfix/1.0.1-error-envio
git push origin main develop --tags
git branch -d hotfix/1.0.1-error-envio
```

Si en ese momento existe una rama `release/*` abierta, el hotfix se
integra en ella en lugar de en `develop`.

### 7.h / 7.i Nueva funcionalidad y deshacer la modificación B

```bash
git checkout develop
git checkout -b feature/nueva-funcionalidad
# modificación A
git commit -am "Modificacion A" && git push -u origin feature/nueva-funcionalidad
# modificación B
git commit -am "Modificacion B" && git push
```

Para volver al estado de A cuando B **ya se publicó**, lo correcto es
`git revert`. Crea un commit nuevo que invierte B y no reescribe la
historia que otros ya pueden haber descargado:

```bash
git revert HEAD      # deshace B y deja el código como estaba en A
git push
```

La alternativa `git reset --hard HEAD~1` seguida de
`git push --force-with-lease` también vuelve a A, pero reescribe la
historia remota. Solo es aceptable en una rama personal que nadie más
usa.

### 7.j Llevar la nueva funcionalidad a producción siguiendo Gitflow

En Gitflow una `feature` **nunca** va directo a `main`. El camino es:

1. `feature/*` → `develop`, mediante un Pull Request revisado.
2. Desde `develop` se abre una nueva `release/1.1.0`.
3. `release/1.1.0` → `main` (tag `v1.1.0`) y → `develop`, igual que en 7.e.

```bash
git checkout develop && git merge --no-ff feature/nueva-funcionalidad
git checkout -b release/1.1.0
git checkout main    && git merge --no-ff release/1.1.0 && git tag -a v1.1.0 -m "Release 1.1.0"
git checkout develop && git merge --no-ff release/1.1.0
git push origin main develop --tags
```

---

### 8. ¿Cómo podemos documentar con Git?

Git documenta el proyecto en varios niveles:

- **Mensajes de commit:** explican *por qué* se hizo cada cambio.
- **Tags anotados:** marcan versiones (`v1.0.0`, `v1.0.1`) con su
  descripción.
- **Nombres de ramas:** muestran la intención del cambio (`feature/`,
  `release/`, `hotfix/`).
- **Archivos versionados junto al código:** `README.md`, `CHANGELOG.md`,
  `CONTRIBUTING.md` y plantillas de `.github/`.
- **En GitHub:** Pull Requests con discusión y revisión, Issues, Releases
  y Wiki.

Como la documentación se versiona igual que el código, cada versión del
software tiene la documentación que le corresponde.

### 8.a ¿Qué documentaríamos en el README?

El README es la puerta de entrada al repositorio. Debe permitir que
alguien que no conoce el proyecto lo entienda, lo ejecute y pueda
contribuir sin ayuda. Según las buenas prácticas debe incluir:

| Elemento                         | Por qué                                                             |
|----------------------------------|---------------------------------------------------------------------|
| Nombre y descripción             | Qué es el sistema y qué problema resuelve.                          |
| Requisitos previos               | Versiones de lenguaje, herramientas y dependencias necesarias.      |
| Instalación y ejecución          | Pasos reproducibles para compilar, correr y probar.                 |
| Configuración                    | Variables de entorno y archivos de ejemplo, sin secretos reales.    |
| Estructura del proyecto          | Dónde está cada cosa.                                               |
| Flujo de ramas y convenciones    | Cómo se trabaja (Gitflow, commits, versionado) para mantener la consistencia. |
| Cómo contribuir                  | Proceso de PR, revisión y CODEOWNERS.                               |
| CI/CD                            | Qué valida el pipeline y cuándo se ejecuta.                         |
| Historial de versiones           | Qué cambió en cada release (o un enlace al CHANGELOG).              |
| Autores y responsables           | A quién consultar.                                                  |
| Licencia                         | En qué condiciones se puede usar el código.                         |

El README está **versionado en el repositorio**, así que evoluciona junto
con el código y sus cambios también pasan por revisión en un PR.

### 8.b Cambios de un colaborador externo: ¿qué datos pedirle y qué ofrece GitHub?

**Datos que le pediríamos al abrir el Pull Request:**

1. **Descripción del cambio:** qué modifica y **por qué** (el problema o
   la necesidad). Sin el "por qué", el revisor no puede evaluar si la
   solución es la adecuada.
2. **Issue vinculado** (`Closes #N`): da trazabilidad entre el
   requerimiento o bug y el código que lo resuelve, y cierra el issue
   automáticamente al mergear.
3. **Tipo de cambio** (feature, fix, refactor, docs): define el impacto
   y la rama destino según Gitflow.
4. **Cómo se probó:** pasos para reproducir y verificar, entorno usado y
   resultados. Permite validar el cambio sin tener que adivinar.
5. **Impacto y riesgos:** si rompe compatibilidad o requiere migraciones
   o pasos extra al desplegar.
6. **Evidencia** (capturas o logs) cuando el cambio es visual o difícil
   de reproducir.
7. **Checklist de calidad:** compila, pasa la CI, tiene pruebas, la
   documentación está actualizada y no incluye configuraciones locales
   ni secretos.

**Qué nos ofrece GitHub:**

- **`.github/pull_request_template.md`:** al abrir un PR, GitHub
  precarga su contenido en la descripción. Así, cualquier persona, incluso
  externa, completa siempre los mismos datos con la misma estructura, y
  el revisor recibe toda la información necesaria desde el principio. La
  plantilla de este repositorio pide descripción, issue vinculado, tipo de
  cambio, rama destino según Gitflow, pruebas realizadas, checklist,
  riesgos y evidencia. También se pueden tener varias plantillas en
  `.github/PULL_REQUEST_TEMPLATE/`.
- **`CODEOWNERS`:** asigna automáticamente como revisores a los
  responsables del código afectado.
- **GitHub Actions (CI):** valida el cambio automáticamente antes de
  mergear, sin depender de que el revisor lo pruebe a mano.
- **Revisión de código:** comentarios por línea, sugerencias aplicables
  con un clic y estados *Approve* o *Request changes*.
- **Branch protection rules:** permiten exigir PR, aprobaciones mínimas y
  CI en verde para poder mergear en `main` o `develop`.
- **Issue templates y `CONTRIBUTING.md`:** explican a los externos cómo
  reportar y contribuir antes de escribir código.
- **Forks:** permiten que un externo proponga cambios sin tener permisos
  de escritura en el repositorio.

En conjunto, estas herramientas aseguran que todo cambio externo llegue
**documentado, trazable, probado y revisado**, que es el objetivo de la
gestión de la configuración.
