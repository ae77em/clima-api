# meli-test-api

Breve descripción de la solución
================================

Se encuentra hosteada en una app de Heroku. Se hicieron 5 endpoints, 4 para obtener los resultados de los cálculos indicados en el enunciado, y un 5to para poder correr el job de cálculo de estados climáticos de los próximos 10 años manualmente.

A continuación se describe cada servicio, su uso, y respuestas.

Clima
-----

Retorna el clima en el día indicado.

Url: <http://meli-test-api.herokuapp.com/clima?dia=150>

Parámetros:

\* dia: entero. Indica el día del cual queremos saber el estado, tomándose como valor mínimo el 0 y máximo el 3599. En el caso de no poner un valor en dicho rango, retorna error.

Respuesta de ejemplo:

```{"clima":"lluvia","dia":150}```

Calcular estados

Simplemente calcula los estado de cada día de aquí a 10 años.

Url: http://meli-test-api.herokuapp.com/calcular-estados

Consideraciones:

-   Se toman años de 360 días, que es lo que tarda el planeta más lento en dar la vuelta alrededor del sol. Se toma para empezar el día del año correspondiente a hoy en el calendario gregoriano, o bien 360, si es que dicho valor es mayor. Esto genera que 5 días al año se calculen los mismos valores. Se solucionaría si se corriera el programa en un planeta con 360 días por año.

-   En el caso de los períodos de **sequía**, para considerar a los planetas alineados entre sí y con el sol, se verificó que estén *perfectamente* alineados, es decir, que los 4 puntos formen parte del mismo vector.

-   En el caso de los períodos de **condiciones óptimas de presión y temperatura**, para considerar a los planetas alineados entre sí, sin estar alineados con el sol, se consideran los 3 puntos, y si el triángulo que forman tiene un área muy pequeña, se los considera alineados, aunque no lo estén *perfectamente*. Se definió arbitrariamente ese valor *muy pequeño* como 30.000, basándose en ver cómo se veían al graficarlos en un plano, usándose un software para tal fin (geogebra).
-   Para los períodos, de **lluvia**, se toma para cada período particular un día de pico de intensidad (es decir, no se toma el máximo general del tamaño del triángulo, sino el de cada período).

Períodos de sequía

Retorna un listado de los períodos de sequía. Cada período a su vez tiene un listado de los días que le corresponden. Además, indica la cantidad de períodos en ese estado.

Url: <http://meli-test-api.herokuapp.com/periodos-sequia>

Respuesta de ejemplo:

{"clima":"sequia","periodos":\[\[899\],\[989\]\],"cantidad\_periodos":2}

Períodos de lluvia

Retorna un listado de los períodos de lluvia. Cada período a su vez tiene un listado de los días que le corresponden. Además, indica la cantidad de períodos en ese estado, y retorna un listado con los días correspondientes a los picos máximos de cada período.

Url: <http://meli-test-api.herokuapp.com/periodos-lluvia>

Respuesta de ejemplo:

```
{
 "clima":"lluvia",
 "periodos":[
   [ 22, 23, 24, 25, 26, 27, 28 ],
   [ 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88 ]
 ],
 "cantidad_periodos":2,
 "picos_maximos": [ 28, 77 ]
}
```

**Períodos de condiciones óptimas**

Retorna un listado de los períodos de sequía. Cada período a su vez tiene un listado de los días que le corresponden. Además, indica la cantidad de períodos en ese estado.

Url: <http://meli-test-api.herokuapp.com/periodos-condiciones-optimas>

Respuesta de ejemplo:

```
{"clima": "condiciones óptimas de presión y temperatura",
 "periodos": [ [852], [946] ],
 "cantidad_periodos":2}
 ```

Es decir, hay dos períodos, de un día cada uno, correspondientes a los días 852 y 946 a partir del día de hoy.
