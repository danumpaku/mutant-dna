# Mutant DNA
Prueba tecnica para Mercado libre, de Daniel Numpaque.

Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Men.
Te ha contratado a ti para que desarrolles un proyecto que detecte si un humano es mutante basándose en su secuencia de ADN.
Para eso te ha pedido crear un programa con un método o función con la siguiente firma (En alguno de los siguiente lenguajes: Java / Golang / C-C++ / Javascript (node) / Python / Ruby):

boolean isMutant(String[] dna); // Ejemplo Java

En donde recibirás como parámetro un array de Strings que representan cada fila de una tabla de (NxN) con la secuencia del ADN. Las letras de los Strings solo pueden ser: (A,T,C,G), las cuales representa cada base nitrogenada del ADN.

Ejemplo No Mutante

- A T G C G A
- C A G T G C
- T T A T T T
- A G A C G G
- G C G T C A
- T C A C T G

Ejemplo Mutante

- A T G C G A
- C A G T G C
- T T A T G T
- A G A A G G
- C C C C T A
- T C A C T G

Sabrás si un humano es mutante, si encuentras más de una secuencia de cuatro letras iguales, de forma oblicua, horizontal o vertical.
Ejemplo (Caso mutante):
String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
En este caso el llamado a la función isMutant(dna) devuelve “true”.
Desarrolla el algoritmo de la manera más eficiente posible.

Desafíos:
# Nivel 1:
Programa (en cualquier lenguaje de programación) que cumpla con el método pedido por Magneto.

# Nivel 2:
Crear una API REST, hostear esa API en un cloud computing libre (Google App Engine, Amazon AWS, etc), crear el servicio “/mutant/” en donde se pueda detectar si un humano es mutante enviando la secuencia de ADN mediante un HTTP POST con un Json el cual tenga el siguiente formato:
POST → /mutant/
{
“dna”:["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}
# En caso de verificar un mutante, debería devolver un HTTP 200-OK, en caso contrario un 403-Forbidden

# Nivel 3:
Anexar una base de datos, la cual guarde los ADN’s verificados con la API.
Solo 1 registro por ADN.
Exponer un servicio extra “/stats” que devuelva un Json con las estadísticas de las verificaciones de ADN: {“count_mutant_dna”:40, “count_human_dna”:100: “ratio”:0.4}
Tener en cuenta que la API puede recibir fluctuaciones agresivas de tráfico (Entre 100 y 1 millón de peticiones por segundo).
Test-Automáticos, Code coverage > 80%.
Entregar:
- Código Fuente (Para Nivel 2 y 3: En repositorio github).
- Instrucciones de cómo ejecutar el programa o la API. (Para Nivel 2 y 3: En README de github).
- URL de la API (Nivel 2 y 3).

# Solución

El método solicitado para el nivel 1 del desafio, fue desarrollado en Java se encuentra en org.brotherhood.mutantdna.helpers.DnaHelper. 
Se optimizó pensando en que en la mayoria de los casos seria necesario recorrer el arreglo completo pero teniendo en cuenta que con solo encontrar 2 secuencias de cuatro letras iguales ya se habria detectado un mutante. El método está documentado para mayor claridad

Debido a las altas fluctuaciones de trafico a las que podria ser sometida la API se optó por utilizar Lambda de AWS. La base de datos es una Aurora Serverless Postgres y se utilizó un secreto en AWS Secret Manager para guardar la información de conexión a la base de datos. La API se expone a traves de un API gateway conectado a la función lambda.
En la carpeta cloudformation se encuentran los scripts de infrastructura asociados a lo descrito.

La API ya está publicada en mi cuenta de AWS, la URL base es:


# Desplegar la API

Si desea desplegar la API en su cuenta de AWS siga los siguientes pasos:
- Descargue el proyecto
- En la ventana de comandos vaya a la carpeta principal del proyecto, donde está pom.xml, y ejecute "mvn package -P assembly-zip" para crear el compilado que se va a desplegar, el cual quedará en "target\Mutant-Dna-1.0-SNAPSHOT-lambda-package.zip".
- En su cuenta de AWS cree un bucket de S3 privado con el nombre "MutantDnaApi-DanielNumpaque"
- Suba al bucket la carpeta "cloudformation" que encontrará en el proyecto.
- Cree en el bucket una carpeta al mismo nivel que la carpeta "cloudformation" y llamela "artifacts"
- Suba a la carpeta artifacts el archivo "Mutant-Dna-1.0-SNAPSHOT-lambda-package.zip"
- Entre a la carpeta "cloudformation" y copie la URL del archivo "MutantDnaApi.yml"
- Vaya a la consola de cloud formation y presione el botón crear stack
- Indique que ya tiene una plantilla y que la va a leer de S3, y pegue la URL del archivo "MutantDnaApi.yml"
- La plantilla le solicitará el nombre de la API el cual puede ser "MutantDnaApi"; y el nombre del bucket de S3, el cual seria "MutantDnaApi-DanielNumpaque"
- Complete el proceso de creación del stack
- Una vez AWS complete la creación del stack, vaya a la consola de Secret Manager
- Busque el secreto "MutantDnaDatabaseConnectionSecret" y copie su ARN
- Vaya a la consola de RDS y abra el query editor
- Conectese a la base de datos "MutantDnaDatabase" utilizando el ARN del secreto "MutantDnaDatabaseConnectionSecret"
- Copie y ejecute el contenido del archivo "bdscripts/create_table_candidate_dna.sql" en el query editor, para crear la tabla que guardará la información del ADN. No se utilizó el mecanismo de actualización automatico de la base de datos de hibernate para reducir el tiempo de encendido del Lambda, lo cual es relevante para soportar un alto trafico.
- El proceso de despliegue ha sido completado

# Ejecutar la API desplegada
Una vez ha desplegado la API puede ir a la consola de ApiGateway, buscar la API "MutantDnaApi" y obtener la URL base para realizar las peticiones que desee.

# Aclaracion
En este momento el script de cloud formation no crea las VPC, subredes y grupos de seguridad necesarios para conectar la RDS con la funcion lambda, cosas que se hicieron a mano para la entrega. para probar la API utilice:
Endpoint Mutants: https://q5pel6qge3.execute-api.us-east-1.amazonaws.com/Labs/mutants
Enpoint Stats: https://q5pel6qge3.execute-api.us-east-1.amazonaws.com/Labs/stats
