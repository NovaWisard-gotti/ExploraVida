package com.educalab.exploravida.data.local.seed

import com.educalab.exploravida.data.local.entity.ExperienceStepEntity
import com.educalab.exploravida.data.local.entity.InteractiveElementEntity
import com.educalab.exploravida.data.local.entity.LearningExperienceEntity
import com.educalab.exploravida.data.local.entity.LivingSystemEntity
import com.educalab.exploravida.data.local.entity.SystemConnectionEntity
import com.educalab.exploravida.domain.model.AnimationKey
import com.educalab.exploravida.domain.model.ExperienceKind
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.SceneBackground
import com.educalab.exploravida.domain.model.Systems

/**
 * Contenido semilla de ExploraVida.
 * Todo el texto esta escrito para ninos de 8 a 12 anos: frases cortas,
 * ejemplos cotidianos y ninguna terminologia medica avanzada.
 */
object SeedContent {

    val systems: List<LivingSystemEntity> = listOf(
        LivingSystemEntity(
            Systems.DIGESTIVO, "Sistema digestivo",
            "Transforma los alimentos para sacar lo que el cuerpo necesita.",
            "#7BE0A5", "ic_leaf", 0
        ),
        LivingSystemEntity(
            Systems.RESPIRATORIO, "Sistema respiratorio",
            "Toma el oxigeno del aire cuando Vita respira.",
            "#6EC6FF", "ic_lungs", 1
        ),
        LivingSystemEntity(
            Systems.CIRCULATORIO, "Sistema circulatorio",
            "Transporta sustancias importantes por todo el cuerpo.",
            "#FF8A80", "ic_heartbeat", 2
        ),
        LivingSystemEntity(
            Systems.MOVIMIENTO, "Sistema de movimiento",
            "Permite caminar, correr, saltar y levantar cosas.",
            "#FFB347", "ic_muscle", 3
        ),
        LivingSystemEntity(
            Systems.LIMPIEZA, "Sistema de limpieza",
            "Saca del cuerpo lo que ya no se necesita.",
            "#67D5C4", "ic_drop", 4
        ),
        LivingSystemEntity(
            Systems.RELACION, "Sistema de relacion",
            "Avisa a Vita de lo que pasa a su alrededor.",
            "#C79BFF", "ic_lens", 5
        )
    )

    val connections: List<SystemConnectionEntity> = listOf(
        SystemConnectionEntity(
            fromSystemId = Systems.DIGESTIVO, toSystemId = Systems.CIRCULATORIO,
            explanation = "Lo bueno del alimento pasa a la sangre, como cuando el agua pasa por un colador."
        ),
        SystemConnectionEntity(
            fromSystemId = Systems.RESPIRATORIO, toSystemId = Systems.CIRCULATORIO,
            explanation = "El oxigeno del aire entra en la sangre y empieza su viaje."
        ),
        SystemConnectionEntity(
            fromSystemId = Systems.CIRCULATORIO, toSystemId = Systems.MOVIMIENTO,
            explanation = "La sangre lleva energia y oxigeno hasta los musculos para que puedan moverse."
        ),
        SystemConnectionEntity(
            fromSystemId = Systems.CIRCULATORIO, toSystemId = Systems.LIMPIEZA,
            explanation = "La sangre recoge lo que sobra y lo lleva hasta los filtros del cuerpo."
        ),
        SystemConnectionEntity(
            fromSystemId = Systems.MOVIMIENTO, toSystemId = Systems.RESPIRATORIO,
            explanation = "Al correr, los musculos piden mas oxigeno y por eso respiras mas rapido."
        ),
        SystemConnectionEntity(
            fromSystemId = Systems.MOVIMIENTO, toSystemId = Systems.CIRCULATORIO,
            explanation = "Cuando te mueves mucho, el corazon late mas fuerte para repartir mas rapido."
        ),
        SystemConnectionEntity(
            fromSystemId = Systems.RELACION, toSystemId = Systems.MOVIMIENTO,
            explanation = "Los sentidos avisan y el cuerpo reacciona: ves la pelota y saltas."
        ),
        SystemConnectionEntity(
            fromSystemId = Systems.DIGESTIVO, toSystemId = Systems.LIMPIEZA,
            explanation = "Lo que el cuerpo no aprovecha del alimento se elimina."
        ),
        SystemConnectionEntity(
            fromSystemId = Systems.CIRCULATORIO, toSystemId = Systems.DIGESTIVO,
            explanation = "La sangre tambien lleva oxigeno al sistema digestivo para que pueda trabajar."
        ),
        SystemConnectionEntity(
            fromSystemId = Systems.RESPIRATORIO, toSystemId = Systems.LIMPIEZA,
            explanation = "Al soltar el aire, el cuerpo tambien deja salir algo que ya no necesita."
        )
    )

    val experiences: List<LearningExperienceEntity> = listOf(
        LearningExperienceEntity(
            "exp_explorar", "Explora a Vita", "Toca y descubre",
            "Toca cualquier zona de Vita. Yo te cuento que hace.",
            ExperienceKind.EXPLORACION.name, 0, 0,
            SceneBackground.LABORATORIO.name, "ic_lens"
        ),
        LearningExperienceEntity(
            "exp_comer", "Cuando Vita come", "Arrastra un alimento",
            "Vamos a descubrir que ocurre cuando Vita come.",
            ExperienceKind.RECORRIDO.name, 1, 0,
            SceneBackground.CUEVA_ESTOMAGO.name, "ic_leaf"
        ),
        LearningExperienceEntity(
            "exp_respirar", "Cuando Vita respira", "Activa la respiracion",
            "Respira conmigo. Mira por donde entra el aire.",
            ExperienceKind.RECORRIDO.name, 2, 20,
            SceneBackground.CAMARA_AIRE.name, "ic_lungs"
        ),
        LearningExperienceEntity(
            "exp_moverse", "Cuando Vita se mueve", "Haz que Vita corra",
            "Que crees que necesita Vita para poder correr?",
            ExperienceKind.RECORRIDO.name, 3, 45,
            SceneBackground.CAMPO_MUSCULO.name, "ic_muscle"
        ),
        LearningExperienceEntity(
            "exp_viaje_energia", "El viaje de la energia", "Sigue la particula",
            "Sigue esta chispa. Ha empezado siendo una fruta.",
            ExperienceKind.RECORRIDO.name, 4, 70,
            SceneBackground.TORRENTE.name, "ic_spark"
        ),
        LearningExperienceEntity(
            "exp_viaje_oxigeno", "El viaje del oxigeno", "Sigue la burbuja",
            "Esta burbuja acaba de entrar con el aire. A donde ira?",
            ExperienceKind.RECORRIDO.name, 5, 95,
            SceneBackground.CIELO.name, "ic_drop"
        ),
        LearningExperienceEntity(
            "exp_conexiones", "Conecta los sistemas", "Une con el dedo",
            "Une dos sistemas que se ayuden entre ellos.",
            ExperienceKind.CONEXION.name, 6, 110,
            SceneBackground.LABORATORIO.name, "ic_link"
        ),
        LearningExperienceEntity(
            "exp_secuencia", "Ordena lo que ocurre", "Coloca las tarjetas",
            "Estas tarjetas se han desordenado. Me ayudas?",
            ExperienceKind.SECUENCIA.name, 7, 135,
            SceneBackground.PAPEL_CUADERNO.name, "ic_path"
        ),
        LearningExperienceEntity(
            "exp_juntos", "Sistemas que trabajan juntos", "Come y despues corre",
            "Vita comera una fruta y despues correra. Observa todo.",
            ExperienceKind.HISTORIA.name, 8, 160,
            SceneBackground.PRADERA.name, "ic_heartbeat"
        ),
        LearningExperienceEntity(
            "exp_comparar", "Que pasa si...", "Compara dos escenarios",
            "Vamos a probar dos veces lo mismo cambiando una cosa.",
            ExperienceKind.COMPARACION.name, 9, 190,
            SceneBackground.LABORATORIO_NOCHE.name, "ic_flask"
        ),
        LearningExperienceEntity(
            "exp_limpieza", "El viaje de lo que sobra", "Sigue el recorrido",
            "No todo lo que entra se aprovecha. Vamos a verlo.",
            ExperienceKind.RECORRIDO.name, 10, 220,
            SceneBackground.TORRENTE.name, "ic_drop"
        ),
        LearningExperienceEntity(
            "exp_sentidos", "Vita y su entorno", "Reacciona al mundo",
            "Vita nota lo que pasa fuera. Y su cuerpo responde.",
            ExperienceKind.HISTORIA.name, 11, 250,
            SceneBackground.PRADERA.name, "ic_lens"
        )
    )

    private fun step(
        experienceId: String, order: Int, title: String, text: String,
        systemId: String?, animation: AnimationKey, illustration: IllustrationKey
    ) = ExperienceStepEntity(
        experienceId = experienceId, orderIndex = order, title = title, text = text,
        systemId = systemId, animationKey = animation.name, illustrationKey = illustration.name
    )

    val steps: List<ExperienceStepEntity> = listOf(
        // --- exp_explorar
        step("exp_explorar", 0, "Un ser vivo entero",
            "Vita es un ser vivo. Tiene partes distintas que trabajan en equipo.",
            null, AnimationKey.ZOOM_CELULA, IllustrationKey.VITA_TRANQUILA),
        step("exp_explorar", 1, "Toca y descubre",
            "Toca una zona iluminada. Cada zona pertenece a un sistema.",
            null, AnimationKey.SISTEMA_ILUMINA, IllustrationKey.LUPA),
        step("exp_explorar", 2, "Nada esta suelto",
            "Cada parte ayuda a las demas. Ninguna trabaja sola.",
            null, AnimationKey.FLECHA_CONEXION, IllustrationKey.VITA_CURIOSA),
        // --- exp_comer
        step("exp_comer", 0, "Llega el alimento",
            "Arrastra una fruta hasta Vita. El alimento entra por la boca.",
            Systems.DIGESTIVO, AnimationKey.ALIMENTO_VIAJA, IllustrationKey.MANZANA),
        step("exp_comer", 1, "Se hace mas pequeno",
            "Al masticar, el alimento se rompe en trozos muy pequenos.",
            Systems.DIGESTIVO, AnimationKey.MASTICAR, IllustrationKey.BOCA),
        step("exp_comer", 2, "Se mezcla por dentro",
            "Dentro del cuerpo el alimento se mezcla y se transforma.",
            Systems.DIGESTIVO, AnimationKey.MEZCLA_ESTOMAGO, IllustrationKey.ESTOMAGO),
        step("exp_comer", 3, "Aparecen sustancias utiles",
            "De esa mezcla salen sustancias utiles, como piezas pequenas de energia.",
            Systems.DIGESTIVO, AnimationKey.LIBERAR_NUTRIENTES, IllustrationKey.NUTRIENTE),
        step("exp_comer", 4, "Empieza el reparto",
            "Esas sustancias pasan a la sangre. Aqui entra otro sistema.",
            Systems.CIRCULATORIO, AnimationKey.FLUJO_SANGRE, IllustrationKey.CAMINOS),
        // --- exp_respirar
        step("exp_respirar", 0, "Entra el aire",
            "Vita toma aire. El aire entra por la nariz.",
            Systems.RESPIRATORIO, AnimationKey.AIRE_ENTRA, IllustrationKey.NARIZ),
        step("exp_respirar", 1, "Los pulmones se llenan",
            "El aire llega a los pulmones, que se hacen mas grandes.",
            Systems.RESPIRATORIO, AnimationKey.BURBUJA_OXIGENO, IllustrationKey.PULMONES),
        step("exp_respirar", 2, "El oxigeno pasa a la sangre",
            "Del aire, el cuerpo aprovecha sobre todo el oxigeno.",
            Systems.CIRCULATORIO, AnimationKey.BURBUJA_OXIGENO, IllustrationKey.BURBUJA_OXIGENO),
        step("exp_respirar", 3, "Y sale el aire",
            "Despues Vita suelta el aire que ya no necesita.",
            Systems.RESPIRATORIO, AnimationKey.AIRE_SALE, IllustrationKey.VITA_RESPIRANDO),
        // --- exp_moverse
        step("exp_moverse", 0, "Vita quiere correr",
            "Para moverse, Vita usa sus musculos.",
            Systems.MOVIMIENTO, AnimationKey.CARRERA, IllustrationKey.VITA_CORRIENDO),
        step("exp_moverse", 1, "Los musculos piden ayuda",
            "Los musculos necesitan energia y oxigeno para trabajar.",
            Systems.MOVIMIENTO, AnimationKey.BOMBEO_MUSCULO, IllustrationKey.MUSCULO),
        step("exp_moverse", 2, "El corazon acelera",
            "El corazon late mas rapido para repartir mas deprisa.",
            Systems.CIRCULATORIO, AnimationKey.LATIDO, IllustrationKey.CORAZON),
        step("exp_moverse", 3, "Respiras mas rapido",
            "Por eso, cuando corres, respiras mas veces por minuto.",
            Systems.RESPIRATORIO, AnimationKey.AIRE_ENTRA, IllustrationKey.PULMONES),
        step("exp_moverse", 4, "Todo a la vez",
            "Moverse no es cosa de un solo sistema. Son varios trabajando juntos.",
            null, AnimationKey.SISTEMA_ILUMINA, IllustrationKey.VITA_CORRIENDO),
        // --- exp_viaje_energia
        step("exp_viaje_energia", 0, "Empieza en el plato",
            "Esta chispa era hace un momento un trozo de fruta.",
            Systems.DIGESTIVO, AnimationKey.ALIMENTO_VIAJA, IllustrationKey.MANZANA),
        step("exp_viaje_energia", 1, "Se transforma",
            "El sistema digestivo la convierte en algo que el cuerpo puede usar.",
            Systems.DIGESTIVO, AnimationKey.LIBERAR_NUTRIENTES, IllustrationKey.NUTRIENTE),
        step("exp_viaje_energia", 2, "Sube al transporte",
            "La sangre la recoge y la lleva de viaje.",
            Systems.CIRCULATORIO, AnimationKey.FLUJO_SANGRE, IllustrationKey.PARTICULA_ENERGIA),
        step("exp_viaje_energia", 3, "Llega al musculo",
            "Llega justo donde hace falta: al musculo que va a moverse.",
            Systems.MOVIMIENTO, AnimationKey.CHISPA_ENERGIA, IllustrationKey.MUSCULO),
        step("exp_viaje_energia", 4, "Se convierte en movimiento",
            "Esa energia se convierte en un salto de Vita.",
            Systems.MOVIMIENTO, AnimationKey.SALTO, IllustrationKey.VITA_CORRIENDO),
        // --- exp_viaje_oxigeno
        step("exp_viaje_oxigeno", 0, "Una burbuja en el aire",
            "El oxigeno estaba en el aire, fuera del cuerpo.",
            Systems.RESPIRATORIO, AnimationKey.AIRE_ENTRA, IllustrationKey.BURBUJA_OXIGENO),
        step("exp_viaje_oxigeno", 1, "Entra con la respiracion",
            "Vita respira y la burbuja entra hasta los pulmones.",
            Systems.RESPIRATORIO, AnimationKey.BURBUJA_OXIGENO, IllustrationKey.PULMONES),
        step("exp_viaje_oxigeno", 2, "Cambia de vehiculo",
            "En los pulmones, la burbuja pasa a la sangre.",
            Systems.CIRCULATORIO, AnimationKey.FLUJO_SANGRE, IllustrationKey.CAMINOS),
        step("exp_viaje_oxigeno", 3, "Recorre el cuerpo",
            "Viaja por caminos muy finos hasta cualquier rincon.",
            Systems.CIRCULATORIO, AnimationKey.LATIDO, IllustrationKey.CORAZON),
        step("exp_viaje_oxigeno", 4, "Llega y ayuda",
            "Junto con la energia, ayuda a que el cuerpo funcione.",
            Systems.MOVIMIENTO, AnimationKey.CHISPA_ENERGIA, IllustrationKey.MUSCULO),
        // --- exp_conexiones
        step("exp_conexiones", 0, "Sistemas companeros",
            "Cada sistema tiene companeros a los que ayuda.",
            null, AnimationKey.FLECHA_CONEXION, IllustrationKey.CUADERNO),
        step("exp_conexiones", 1, "Une con el dedo",
            "Arrastra desde un sistema hasta otro para probar la conexion.",
            null, AnimationKey.FLECHA_CONEXION, IllustrationKey.LUPA),
        step("exp_conexiones", 2, "Cada union tiene explicacion",
            "Si la union existe, te cuento por que funciona.",
            null, AnimationKey.SISTEMA_ILUMINA, IllustrationKey.NORA),
        // --- exp_secuencia
        step("exp_secuencia", 0, "Todo tiene un orden",
            "En el cuerpo las cosas ocurren en un orden.",
            null, AnimationKey.FLECHA_CONEXION, IllustrationKey.CUADERNO),
        step("exp_secuencia", 1, "Coloca las tarjetas",
            "Toca una tarjeta para colocarla en el siguiente hueco.",
            null, AnimationKey.SISTEMA_ILUMINA, IllustrationKey.PROBETA),
        // --- exp_juntos
        step("exp_juntos", 0, "Vita come una fruta",
            "Primero come. El sistema digestivo empieza a trabajar.",
            Systems.DIGESTIVO, AnimationKey.MASTICAR, IllustrationKey.MANZANA),
        step("exp_juntos", 1, "Se reparten las sustancias",
            "La sangre reparte lo bueno del alimento por el cuerpo.",
            Systems.CIRCULATORIO, AnimationKey.FLUJO_SANGRE, IllustrationKey.NUTRIENTE),
        step("exp_juntos", 2, "Vita empieza a correr",
            "Los musculos se ponen en marcha y gastan energia.",
            Systems.MOVIMIENTO, AnimationKey.CARRERA, IllustrationKey.VITA_CORRIENDO),
        step("exp_juntos", 3, "Respira mas rapido",
            "Necesita mas oxigeno, asi que respira mas veces.",
            Systems.RESPIRATORIO, AnimationKey.AIRE_ENTRA, IllustrationKey.PULMONES),
        step("exp_juntos", 4, "El corazon acompana",
            "El corazon late mas fuerte para llegar a todas partes.",
            Systems.CIRCULATORIO, AnimationKey.LATIDO, IllustrationKey.CORAZON),
        step("exp_juntos", 5, "Y se limpia lo que sobra",
            "Al final, el cuerpo aparta lo que ya no le sirve.",
            Systems.LIMPIEZA, AnimationKey.FILTRO_LIMPIEZA, IllustrationKey.FILTRO),
        // --- exp_comparar
        step("exp_comparar", 0, "Dos pruebas",
            "Haremos la misma carrera dos veces, cambiando una cosa.",
            null, AnimationKey.CARRERA, IllustrationKey.MATRAZ),
        step("exp_comparar", 1, "Observa la diferencia",
            "Fijate en cuanto aguanta Vita en cada prueba.",
            null, AnimationKey.CHISPA_ENERGIA, IllustrationKey.VITA_CORRIENDO),
        // --- exp_limpieza
        step("exp_limpieza", 0, "No todo se aprovecha",
            "De lo que entra, una parte no le sirve al cuerpo.",
            Systems.DIGESTIVO, AnimationKey.LIBERAR_NUTRIENTES, IllustrationKey.DESECHO),
        step("exp_limpieza", 1, "La sangre lo recoge",
            "La sangre recoge esos restos mientras viaja.",
            Systems.CIRCULATORIO, AnimationKey.FLUJO_SANGRE, IllustrationKey.CAMINOS),
        step("exp_limpieza", 2, "Pasa por los filtros",
            "Unos filtros separan lo que sobra del resto.",
            Systems.LIMPIEZA, AnimationKey.FILTRO_LIMPIEZA, IllustrationKey.FILTRO),
        step("exp_limpieza", 3, "Sale del cuerpo",
            "Al final sale del cuerpo, como cuando bebes mucha agua.",
            Systems.LIMPIEZA, AnimationKey.GOTA_AGUA, IllustrationKey.GOTA),
        // --- exp_sentidos
        step("exp_sentidos", 0, "Algo pasa fuera",
            "Vita nota un ruido o una luz cerca.",
            Systems.RELACION, AnimationKey.ONDA_SENTIDO, IllustrationKey.VITA_CURIOSA),
        step("exp_sentidos", 1, "El aviso viaja",
            "Ese aviso llega rapido al resto del cuerpo.",
            Systems.RELACION, AnimationKey.ONDA_SENTIDO, IllustrationKey.CAMINOS),
        step("exp_sentidos", 2, "El cuerpo responde",
            "Vita se aparta o se acerca. El movimiento es la respuesta.",
            Systems.MOVIMIENTO, AnimationKey.SALTO, IllustrationKey.VITA_CORRIENDO),
        step("exp_sentidos", 3, "Relacionarse tambien es vivir",
            "Los seres vivos responden a lo que ocurre a su alrededor.",
            null, AnimationKey.CELEBRACION, IllustrationKey.VITA_TRANQUILA)
    )

    private fun element(
        id: String, name: String, systemId: String, description: String,
        x: Float, y: Float, illustration: IllustrationKey, radius: Float = 0.062f
    ) = InteractiveElementEntity(id, name, systemId, description, x, y, radius, illustration.name)

    val elements: List<InteractiveElementEntity> = listOf(
        element("el_boca", "Boca", Systems.DIGESTIVO,
            "Por aqui entra el alimento y empieza a hacerse pequenito.", 0.50f, 0.235f, IllustrationKey.BOCA),
        element("el_tubo", "Tubo del alimento", Systems.DIGESTIVO,
            "Un tobogan blando que lleva el alimento hacia dentro.", 0.50f, 0.325f, IllustrationKey.TUBO),
        element("el_estomago", "Bolsa mezcladora", Systems.DIGESTIVO,
            "Aqui el alimento se mezcla hasta quedar como un pure.", 0.41f, 0.44f, IllustrationKey.ESTOMAGO),
        element("el_intestino", "Camino largo", Systems.DIGESTIVO,
            "Un camino muy largo donde se recogen las sustancias utiles.", 0.50f, 0.60f, IllustrationKey.INTESTINO),
        element("el_gusto", "Sabor", Systems.DIGESTIVO,
            "El sabor avisa a Vita de que esta comiendo.", 0.56f, 0.265f, IllustrationKey.BOCA),
        element("el_nariz", "Nariz", Systems.RESPIRATORIO,
            "Por aqui entra el aire, limpio y calentito.", 0.575f, 0.195f, IllustrationKey.NARIZ),
        element("el_via_aire", "Tubo del aire", Systems.RESPIRATORIO,
            "El aire baja por un tubo distinto al del alimento.", 0.50f, 0.29f, IllustrationKey.TUBO),
        element("el_pulmon_izq", "Bolsa de aire izquierda", Systems.RESPIRATORIO,
            "Se hincha cuando Vita toma aire.", 0.375f, 0.365f, IllustrationKey.PULMONES),
        element("el_pulmon_der", "Bolsa de aire derecha", Systems.RESPIRATORIO,
            "Se deshincha cuando Vita suelta el aire.", 0.625f, 0.365f, IllustrationKey.PULMONES),
        element("el_corazon", "Bomba", Systems.CIRCULATORIO,
            "Late sin parar y empuja la sangre por todo el cuerpo.", 0.50f, 0.415f, IllustrationKey.CORAZON),
        element("el_camino_alto", "Camino de subida", Systems.CIRCULATORIO,
            "Lleva sustancias hacia la parte de arriba de Vita.", 0.295f, 0.30f, IllustrationKey.CAMINOS),
        element("el_camino_bajo", "Camino de bajada", Systems.CIRCULATORIO,
            "Lleva sustancias hacia la parte de abajo.", 0.695f, 0.545f, IllustrationKey.CAMINOS),
        element("el_red_fina", "Caminos finos", Systems.CIRCULATORIO,
            "Caminos tan finos como un hilo que llegan a todas partes.", 0.50f, 0.715f, IllustrationKey.CAMINOS),
        element("el_vaso_pequeno", "Curva del camino", Systems.CIRCULATORIO,
            "La sangre da una vuelta completa una y otra vez.", 0.725f, 0.30f, IllustrationKey.CAMINOS),
        element("el_brazo_izq", "Musculo del brazo", Systems.MOVIMIENTO,
            "Se encoge y se estira para levantar cosas.", 0.235f, 0.44f, IllustrationKey.MUSCULO),
        element("el_brazo_der", "Musculo del otro brazo", Systems.MOVIMIENTO,
            "Trabaja en pareja con el otro brazo.", 0.765f, 0.44f, IllustrationKey.MUSCULO),
        element("el_pierna_izq", "Musculo de la pierna", Systems.MOVIMIENTO,
            "Empuja el suelo para que Vita avance.", 0.42f, 0.815f, IllustrationKey.MUSCULO),
        element("el_pierna_der", "Musculo de la otra pierna", Systems.MOVIMIENTO,
            "Se turnan: uno empuja mientras el otro descansa.", 0.58f, 0.815f, IllustrationKey.MUSCULO),
        element("el_soporte", "Soporte del cuerpo", Systems.MOVIMIENTO,
            "Una parte dura que sujeta a Vita, como el armazon de una tienda.", 0.50f, 0.675f, IllustrationKey.MUSCULO),
        element("el_articulacion", "Bisagra", Systems.MOVIMIENTO,
            "Permite doblar el cuerpo, como la bisagra de una puerta.", 0.30f, 0.715f, IllustrationKey.MUSCULO),
        element("el_filtro_izq", "Filtro izquierdo", Systems.LIMPIEZA,
            "Separa lo que sobra de lo que aun sirve.", 0.375f, 0.625f, IllustrationKey.FILTRO),
        element("el_filtro_der", "Filtro derecho", Systems.LIMPIEZA,
            "Trabaja en pareja con el otro filtro.", 0.625f, 0.625f, IllustrationKey.FILTRO),
        element("el_salida", "Salida", Systems.LIMPIEZA,
            "Por aqui sale lo que el cuerpo ya no necesita.", 0.50f, 0.885f, IllustrationKey.GOTA),
        element("el_poros", "Piel que suda", Systems.LIMPIEZA,
            "Al sudar tambien sale agua y el cuerpo se refresca.", 0.285f, 0.575f, IllustrationKey.GOTA),
        element("el_ojos", "Ojos", Systems.RELACION,
            "Ven lo que ocurre cerca y avisan enseguida.", 0.445f, 0.155f, IllustrationKey.VITA_CURIOSA),
        element("el_oido", "Oido", Systems.RELACION,
            "Escucha los sonidos del laboratorio.", 0.345f, 0.145f, IllustrationKey.VITA_CURIOSA),
        element("el_antena", "Antena sensora", Systems.RELACION,
            "Nota los cambios del aire, como un bigote de gato.", 0.50f, 0.075f, IllustrationKey.VITA_CURIOSA),
        element("el_piel", "Piel", Systems.RELACION,
            "Nota el frio, el calor y las cosquillas.", 0.775f, 0.60f, IllustrationKey.VITA_TRANQUILA),
        element("el_tacto", "Punta sensible", Systems.RELACION,
            "Con ella Vita toca las cosas con cuidado.", 0.80f, 0.72f, IllustrationKey.VITA_TRANQUILA),
        element("el_centro", "Centro de avisos", Systems.RELACION,
            "Recibe los avisos y decide que hacer.", 0.50f, 0.13f, IllustrationKey.VITA_CURIOSA)
    )
}
