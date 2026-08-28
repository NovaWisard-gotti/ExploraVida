package com.educalab.exploravida.data.local.seed

import com.educalab.exploravida.data.local.entity.ActivityEntity
import com.educalab.exploravida.data.local.entity.BadgeEntity
import com.educalab.exploravida.data.local.entity.ConnectionChallengeEntity
import com.educalab.exploravida.data.local.entity.SequenceEntity
import com.educalab.exploravida.data.local.entity.SequenceItemEntity
import com.educalab.exploravida.domain.engine.RewardEngine
import com.educalab.exploravida.domain.model.ActivityKind
import com.educalab.exploravida.domain.model.IllustrationKey
import com.educalab.exploravida.domain.model.Systems

/** Actividades interactivas, secuencias, retos de conexion e insignias. */
object SeedActivities {

    val activities: List<ActivityEntity> = listOf(
        ActivityEntity("act_comer_arrastrar", "exp_comer", ActivityKind.ARRASTRAR.name,
            "Dale de comer a Vita",
            "Arrastra un alimento hasta Vita para empezar el recorrido.",
            "Vita lleva toda la manana explorando y tiene hambre.", 1, 15),
        ActivityEntity("act_comer_orden", "exp_comer", ActivityKind.ORDENAR.name,
            "Que ocurre primero?",
            "Coloca las tarjetas en el orden correcto.",
            "Las notas del cuaderno se han caido y se han mezclado.", 1, 15),
        ActivityEntity("act_comer_predecir", "exp_comer", ActivityKind.PREDECIR.name,
            "Adonde va lo bueno del alimento?",
            "Elige el sistema que recibira las sustancias utiles.",
            "El alimento ya se ha transformado dentro de Vita.", 2, 15),
        ActivityEntity("act_respirar_observar", "exp_respirar", ActivityKind.OBSERVAR.name,
            "Cuenta las respiraciones",
            "Toca a Vita cada vez que toma aire.",
            "Vita esta tumbada y tranquila en el laboratorio.", 1, 15),
        ActivityEntity("act_respirar_orden", "exp_respirar", ActivityKind.ORDENAR.name,
            "El camino del aire",
            "Ordena el recorrido que hace el aire.",
            "Nora ha dibujado el camino del aire, pero sin numeros.", 2, 15),
        ActivityEntity("act_respirar_conectar", "exp_respirar", ActivityKind.CONECTAR.name,
            "Quien recoge el oxigeno?",
            "Une el sistema respiratorio con quien transporta el oxigeno.",
            "La burbuja de oxigeno esta esperando en los pulmones.", 2, 20),
        ActivityEntity("act_mover_predecir", "exp_moverse", ActivityKind.PREDECIR.name,
            "Que necesita un musculo?",
            "Marca lo que hace falta para que un musculo trabaje.",
            "Vita se prepara para saltar desde una piedra.", 2, 15),
        ActivityEntity("act_mover_orden", "exp_moverse", ActivityKind.ORDENAR.name,
            "Antes de correr",
            "Ordena lo que ocurre justo antes de que Vita corra.",
            "Vita ve una mariposa y quiere seguirla.", 2, 15),
        ActivityEntity("act_mover_comparar", "exp_moverse", ActivityKind.COMPARAR.name,
            "Caminar o correr?",
            "Compara que pasa dentro de Vita en cada caso.",
            "Vita hace el mismo camino andando y despues corriendo.", 2, 20),
        ActivityEntity("act_energia_arrastrar", "exp_viaje_energia", ActivityKind.ARRASTRAR.name,
            "Lleva la chispa",
            "Arrastra la chispa de energia por cada punto del recorrido.",
            "La chispa acaba de salir del sistema digestivo.", 2, 20),
        ActivityEntity("act_energia_orden", "exp_viaje_energia", ActivityKind.ORDENAR.name,
            "El viaje completo",
            "Ordena el viaje de la energia desde la fruta hasta el salto.",
            "Nora quiere pegar el recorrido en su cuaderno.", 3, 20),
        ActivityEntity("act_oxigeno_observar", "exp_viaje_oxigeno", ActivityKind.OBSERVAR.name,
            "Sigue la burbuja",
            "Toca la burbuja en cada parada de su viaje.",
            "Una burbuja de oxigeno entra con la siguiente respiracion.", 2, 20),
        ActivityEntity("act_oxigeno_conectar", "exp_viaje_oxigeno", ActivityKind.CONECTAR.name,
            "De donde a donde?",
            "Une los sistemas que participan en el viaje del oxigeno.",
            "El oxigeno ha cambiado de vehiculo dentro del cuerpo.", 3, 20),
        ActivityEntity("act_conexiones_libre", "exp_conexiones", ActivityKind.CONECTAR.name,
            "Descubre las conexiones",
            "Une parejas de sistemas y descubre por que se ayudan.",
            "El panel de conexiones del laboratorio esta vacio.", 3, 25),
        ActivityEntity("act_conexiones_reto", "exp_conexiones", ActivityKind.CONECTAR.name,
            "Reto de conexiones",
            "Encuentra tres conexiones concretas.",
            "Nora ha marcado tres huecos en el panel.", 3, 25),
        ActivityEntity("act_secuencia_gran", "exp_secuencia", ActivityKind.ORDENAR.name,
            "De la fruta al salto",
            "Ordena las seis tarjetas del gran recorrido.",
            "Es el recorrido mas largo del cuaderno.", 3, 25),
        ActivityEntity("act_juntos_comparar", "exp_juntos", ActivityKind.COMPARAR.name,
            "Con desayuno o sin desayuno?",
            "Compara las dos carreras de Vita.",
            "Vita corre por la pradera dos mananas distintas.", 3, 25),
        ActivityEntity("act_comparar_aire", "exp_comparar", ActivityKind.COMPARAR.name,
            "Mucho aire o poco aire?",
            "Compara la simulacion con mas y con menos oxigeno.",
            "En el laboratorio se puede regular el aire de la camara.", 3, 25),
        ActivityEntity("act_limpieza_predecir", "exp_limpieza", ActivityKind.PREDECIR.name,
            "Que hace el cuerpo con lo que sobra?",
            "Elige que ocurre con los restos.",
            "Vita ha bebido mucha agua despues de correr.", 2, 20),
        ActivityEntity("act_sentidos_comparar", "exp_sentidos", ActivityKind.COMPARAR.name,
            "Nota o no nota?",
            "Compara a Vita atenta y a Vita distraida.",
            "Una hoja cae cerca de Vita mientras explora.", 2, 20)
    )

    val sequences: List<SequenceEntity> = listOf(
        SequenceEntity("seq_comer", "act_comer_orden", "Cuando Vita come",
            "El alimento entra, se transforma y despues se reparte por el cuerpo."),
        SequenceEntity("seq_aire", "act_respirar_orden", "El camino del aire",
            "El aire entra, llega a los pulmones y el oxigeno pasa a la sangre."),
        SequenceEntity("seq_mover", "act_mover_orden", "Antes de correr",
            "Primero llega la energia y el oxigeno, y despues los musculos trabajan."),
        SequenceEntity("seq_energia", "act_energia_orden", "El viaje de la energia",
            "La energia sale del alimento, viaja en la sangre y termina en movimiento."),
        SequenceEntity("seq_gran", "act_secuencia_gran", "De la fruta al salto",
            "Comer, digerir, transportar, respirar, obtener energia y moverse.")
    )

    private fun item(sequenceId: String, label: String, position: Int, systemId: String?, illustration: IllustrationKey) =
        SequenceItemEntity(
            sequenceId = sequenceId, label = label, correctPosition = position,
            systemId = systemId, illustrationKey = illustration.name
        )

    val sequenceItems: List<SequenceItemEntity> = listOf(
        item("seq_comer", "Vita muerde la fruta", 0, Systems.DIGESTIVO, IllustrationKey.MANZANA),
        item("seq_comer", "El alimento se mezcla", 1, Systems.DIGESTIVO, IllustrationKey.ESTOMAGO),
        item("seq_comer", "Aparecen sustancias utiles", 2, Systems.DIGESTIVO, IllustrationKey.NUTRIENTE),
        item("seq_comer", "La sangre las reparte", 3, Systems.CIRCULATORIO, IllustrationKey.CAMINOS),

        item("seq_aire", "Entra el aire por la nariz", 0, Systems.RESPIRATORIO, IllustrationKey.NARIZ),
        item("seq_aire", "Los pulmones se llenan", 1, Systems.RESPIRATORIO, IllustrationKey.PULMONES),
        item("seq_aire", "El oxigeno pasa a la sangre", 2, Systems.CIRCULATORIO, IllustrationKey.BURBUJA_OXIGENO),
        item("seq_aire", "Vita suelta el aire", 3, Systems.RESPIRATORIO, IllustrationKey.VITA_RESPIRANDO),

        item("seq_mover", "La sangre trae energia", 0, Systems.CIRCULATORIO, IllustrationKey.PARTICULA_ENERGIA),
        item("seq_mover", "Tambien llega oxigeno", 1, Systems.RESPIRATORIO, IllustrationKey.BURBUJA_OXIGENO),
        item("seq_mover", "El musculo se prepara", 2, Systems.MOVIMIENTO, IllustrationKey.MUSCULO),
        item("seq_mover", "Vita corre", 3, Systems.MOVIMIENTO, IllustrationKey.VITA_CORRIENDO),

        item("seq_energia", "Un trozo de fruta", 0, Systems.DIGESTIVO, IllustrationKey.MANZANA),
        item("seq_energia", "Se transforma dentro", 1, Systems.DIGESTIVO, IllustrationKey.NUTRIENTE),
        item("seq_energia", "Viaja en la sangre", 2, Systems.CIRCULATORIO, IllustrationKey.PARTICULA_ENERGIA),
        item("seq_energia", "Llega al musculo", 3, Systems.MOVIMIENTO, IllustrationKey.MUSCULO),
        item("seq_energia", "Vita salta", 4, Systems.MOVIMIENTO, IllustrationKey.VITA_CORRIENDO),

        item("seq_gran", "Vita come una fruta", 0, Systems.DIGESTIVO, IllustrationKey.MANZANA),
        item("seq_gran", "El alimento se transforma", 1, Systems.DIGESTIVO, IllustrationKey.ESTOMAGO),
        item("seq_gran", "La sangre transporta", 2, Systems.CIRCULATORIO, IllustrationKey.CAMINOS),
        item("seq_gran", "Vita respira mas fuerte", 3, Systems.RESPIRATORIO, IllustrationKey.PULMONES),
        item("seq_gran", "El musculo recibe energia", 4, Systems.MOVIMIENTO, IllustrationKey.PARTICULA_ENERGIA),
        item("seq_gran", "Vita corre por la pradera", 5, Systems.MOVIMIENTO, IllustrationKey.VITA_CORRIENDO)
    )

    val connectionChallenges: List<ConnectionChallengeEntity> = listOf(
        ConnectionChallengeEntity(
            activityId = "act_respirar_conectar",
            fromSystemId = Systems.RESPIRATORIO, toSystemId = Systems.CIRCULATORIO,
            explanation = "El oxigeno pasa del aire a la sangre y empieza su viaje."
        ),
        ConnectionChallengeEntity(
            activityId = "act_oxigeno_conectar",
            fromSystemId = Systems.RESPIRATORIO, toSystemId = Systems.CIRCULATORIO,
            explanation = "Primero el oxigeno cambia de vehiculo: del aire a la sangre."
        ),
        ConnectionChallengeEntity(
            activityId = "act_oxigeno_conectar",
            fromSystemId = Systems.CIRCULATORIO, toSystemId = Systems.MOVIMIENTO,
            explanation = "Despues la sangre lo entrega donde hace falta: en los musculos."
        ),
        ConnectionChallengeEntity(
            activityId = "act_conexiones_reto",
            fromSystemId = Systems.DIGESTIVO, toSystemId = Systems.CIRCULATORIO,
            explanation = "Lo bueno del alimento pasa a la sangre."
        ),
        ConnectionChallengeEntity(
            activityId = "act_conexiones_reto",
            fromSystemId = Systems.CIRCULATORIO, toSystemId = Systems.LIMPIEZA,
            explanation = "La sangre lleva hasta los filtros lo que ya no sirve."
        ),
        ConnectionChallengeEntity(
            activityId = "act_conexiones_reto",
            fromSystemId = Systems.MOVIMIENTO, toSystemId = Systems.RESPIRATORIO,
            explanation = "Al moverse mucho, el cuerpo pide mas aire."
        )
    )

    val badges: List<BadgeEntity> = listOf(
        BadgeEntity("bdg_primer", "Primer Descubrimiento",
            "Guardaste tu primera pegatina en el cuaderno.", "ic_spark",
            RewardEngine.Rules.PRIMER_DESCUBRIMIENTO, 1),
        BadgeEntity("bdg_aire", "Explorador del Aire",
            "Completaste el recorrido de la respiracion.", "ic_lungs",
            RewardEngine.Rules.RECORRIDOS, 2),
        BadgeEntity("bdg_energia", "Viajero de la Energia",
            "Seguiste la energia desde la fruta hasta el salto.", "ic_spark",
            RewardEngine.Rules.RECORRIDOS, 3),
        BadgeEntity("bdg_recorrido", "Maestro del Recorrido",
            "Terminaste cinco recorridos completos.", "ic_path",
            RewardEngine.Rules.RECORRIDOS, 5),
        BadgeEntity("bdg_conector", "Conector de Sistemas",
            "Descubriste cinco conexiones verdaderas.", "ic_link",
            RewardEngine.Rules.CONEXIONES, 5),
        BadgeEntity("bdg_observador", "Gran Observador",
            "Exploraste quince zonas del cuerpo de Vita.", "ic_lens",
            RewardEngine.Rules.ELEMENTOS, 15),
        BadgeEntity("bdg_movimiento", "Explorador del Movimiento",
            "Resolviste tres secuencias sobre el movimiento.", "ic_muscle",
            RewardEngine.Rules.SECUENCIAS, 3),
        BadgeEntity("bdg_curioso", "Cientifico Curioso",
            "Completaste ocho actividades del laboratorio.", "ic_flask",
            RewardEngine.Rules.ACTIVIDADES, 8),
        BadgeEntity("bdg_conexiones_todas", "Descubridor de Conexiones",
            "Encontraste las diez conexiones del panel.", "ic_link",
            RewardEngine.Rules.CONEXIONES, 10),
        BadgeEntity("bdg_aventurero", "Aventurero de la Vida",
            "Visitaste los seis sistemas de Vita.", "ic_leaf",
            RewardEngine.Rules.TODOS_LOS_SISTEMAS, 6),
        BadgeEntity("bdg_gran_explorador", "Gran Explorador",
            "Completaste ocho experiencias.", "ic_badge",
            RewardEngine.Rules.EXPERIENCIAS, 8),
        BadgeEntity("bdg_maestro", "Maestro ExploraVida",
            "Conseguiste todas las demas insignias.", "ic_badge",
            RewardEngine.Rules.MAESTRO, 0)
    )

    /** 15 conceptos principales. Cada uno es una pagina del cuaderno. */
    data class Concept(val key: String, val title: String, val text: String, val sticker: String)

    val concepts: List<Concept> = listOf(
        Concept("ser_vivo", "Ser vivo",
            "Un ser vivo nace, crece, se alimenta y responde a lo que pasa fuera.", "ic_leaf"),
        Concept("sistema", "Sistema",
            "Un sistema es un grupo de partes que trabajan juntas con una tarea.", "ic_link"),
        Concept("alimento", "Alimento",
            "El alimento trae lo que el cuerpo necesita para funcionar.", "ic_leaf"),
        Concept("digestion", "Digestion",
            "Es transformar el alimento hasta que el cuerpo pueda usarlo.", "ic_flask"),
        Concept("nutriente", "Sustancia util",
            "Son las piezas pequenas que el cuerpo aprovecha del alimento.", "ic_spark"),
        Concept("respiracion", "Respiracion",
            "Es tomar aire y despues soltarlo. Ocurre todo el dia.", "ic_lungs"),
        Concept("oxigeno", "Oxigeno",
            "Es la parte del aire que el cuerpo mas aprovecha.", "ic_drop"),
        Concept("circulacion", "Circulacion",
            "Es el reparto: la sangre lleva cosas de un sitio a otro.", "ic_heartbeat"),
        Concept("energia", "Energia",
            "Es lo que permite moverse, crecer y mantenerse caliente.", "ic_spark"),
        Concept("movimiento", "Movimiento",
            "Los musculos se encogen y se estiran, y el cuerpo se mueve.", "ic_muscle"),
        Concept("limpieza", "Limpieza del cuerpo",
            "Lo que no sirve se separa y sale del cuerpo.", "ic_drop"),
        Concept("relacion", "Relacion con el entorno",
            "Los sentidos avisan y el cuerpo responde.", "ic_lens"),
        Concept("conexion", "Conexion",
            "Dos sistemas se conectan cuando uno entrega algo al otro.", "ic_link"),
        Concept("cooperacion", "Cooperacion",
            "Ningun sistema trabaja solo: todos se ayudan.", "ic_badge"),
        Concept("recorrido", "Recorrido",
            "Muchas cosas del cuerpo hacen un viaje con paradas.", "ic_path")
    )
}
