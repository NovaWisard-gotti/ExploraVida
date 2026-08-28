-- ============================================================
-- ExploraVida 1.0.0 - datos semilla
-- Generado desde SeedContent.kt y SeedActivities.kt (tools/generate_sql.py)
-- La aplicacion siembra estos mismos datos en Room la primera vez que se abre.
-- ============================================================

PRAGMA foreign_keys = ON;

-- living_system (6 filas)
INSERT INTO living_system (id, name, shortDescription, colorHex, iconKey, orderIndex) VALUES ('digestivo', 'Sistema digestivo', 'Transforma los alimentos para sacar lo que el cuerpo necesita.', '#7BE0A5', 'ic_leaf', 0);
INSERT INTO living_system (id, name, shortDescription, colorHex, iconKey, orderIndex) VALUES ('respiratorio', 'Sistema respiratorio', 'Toma el oxigeno del aire cuando Vita respira.', '#6EC6FF', 'ic_lungs', 1);
INSERT INTO living_system (id, name, shortDescription, colorHex, iconKey, orderIndex) VALUES ('circulatorio', 'Sistema circulatorio', 'Transporta sustancias importantes por todo el cuerpo.', '#FF8A80', 'ic_heartbeat', 2);
INSERT INTO living_system (id, name, shortDescription, colorHex, iconKey, orderIndex) VALUES ('movimiento', 'Sistema de movimiento', 'Permite caminar, correr, saltar y levantar cosas.', '#FFB347', 'ic_muscle', 3);
INSERT INTO living_system (id, name, shortDescription, colorHex, iconKey, orderIndex) VALUES ('limpieza', 'Sistema de limpieza', 'Saca del cuerpo lo que ya no se necesita.', '#67D5C4', 'ic_drop', 4);
INSERT INTO living_system (id, name, shortDescription, colorHex, iconKey, orderIndex) VALUES ('relacion', 'Sistema de relacion', 'Avisa a Vita de lo que pasa a su alrededor.', '#C79BFF', 'ic_lens', 5);

-- system_connection (10 filas)
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('digestivo', 'circulatorio', 'Lo bueno del alimento pasa a la sangre, como cuando el agua pasa por un colador.');
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('respiratorio', 'circulatorio', 'El oxigeno del aire entra en la sangre y empieza su viaje.');
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('circulatorio', 'movimiento', 'La sangre lleva energia y oxigeno hasta los musculos para que puedan moverse.');
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('circulatorio', 'limpieza', 'La sangre recoge lo que sobra y lo lleva hasta los filtros del cuerpo.');
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('movimiento', 'respiratorio', 'Al correr, los musculos piden mas oxigeno y por eso respiras mas rapido.');
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('movimiento', 'circulatorio', 'Cuando te mueves mucho, el corazon late mas fuerte para repartir mas rapido.');
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('relacion', 'movimiento', 'Los sentidos avisan y el cuerpo reacciona: ves la pelota y saltas.');
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('digestivo', 'limpieza', 'Lo que el cuerpo no aprovecha del alimento se elimina.');
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('circulatorio', 'digestivo', 'La sangre tambien lleva oxigeno al sistema digestivo para que pueda trabajar.');
INSERT INTO system_connection (fromSystemId, toSystemId, explanation) VALUES ('respiratorio', 'limpieza', 'Al soltar el aire, el cuerpo tambien deja salir algo que ya no necesita.');

-- learning_experience (12 filas)
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_explorar', 'Explora a Vita', 'Toca y descubre', 'Toca cualquier zona de Vita. Yo te cuento que hace.', 'EXPLORACION', 0, 0, 'LABORATORIO', 'ic_lens');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_comer', 'Cuando Vita come', 'Arrastra un alimento', 'Vamos a descubrir que ocurre cuando Vita come.', 'RECORRIDO', 1, 0, 'CUEVA_ESTOMAGO', 'ic_leaf');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_respirar', 'Cuando Vita respira', 'Activa la respiracion', 'Respira conmigo. Mira por donde entra el aire.', 'RECORRIDO', 2, 20, 'CAMARA_AIRE', 'ic_lungs');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_moverse', 'Cuando Vita se mueve', 'Haz que Vita corra', 'Que crees que necesita Vita para poder correr?', 'RECORRIDO', 3, 45, 'CAMPO_MUSCULO', 'ic_muscle');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_viaje_energia', 'El viaje de la energia', 'Sigue la particula', 'Sigue esta chispa. Ha empezado siendo una fruta.', 'RECORRIDO', 4, 70, 'TORRENTE', 'ic_spark');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_viaje_oxigeno', 'El viaje del oxigeno', 'Sigue la burbuja', 'Esta burbuja acaba de entrar con el aire. A donde ira?', 'RECORRIDO', 5, 95, 'CIELO', 'ic_drop');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_conexiones', 'Conecta los sistemas', 'Une con el dedo', 'Une dos sistemas que se ayuden entre ellos.', 'CONEXION', 6, 110, 'LABORATORIO', 'ic_link');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_secuencia', 'Ordena lo que ocurre', 'Coloca las tarjetas', 'Estas tarjetas se han desordenado. Me ayudas?', 'SECUENCIA', 7, 135, 'PAPEL_CUADERNO', 'ic_path');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_juntos', 'Sistemas que trabajan juntos', 'Come y despues corre', 'Vita comera una fruta y despues correra. Observa todo.', 'HISTORIA', 8, 160, 'PRADERA', 'ic_heartbeat');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_comparar', 'Que pasa si...', 'Compara dos escenarios', 'Vamos a probar dos veces lo mismo cambiando una cosa.', 'COMPARACION', 9, 190, 'LABORATORIO_NOCHE', 'ic_flask');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_limpieza', 'El viaje de lo que sobra', 'Sigue el recorrido', 'No todo lo que entra se aprovecha. Vamos a verlo.', 'RECORRIDO', 10, 220, 'TORRENTE', 'ic_drop');
INSERT INTO learning_experience (id, title, subtitle, noraIntro, kind, orderIndex, requiredXp, backgroundKey, iconKey) VALUES ('exp_sentidos', 'Vita y su entorno', 'Reacciona al mundo', 'Vita nota lo que pasa fuera. Y su cuerpo responde.', 'HISTORIA', 11, 250, 'PRADERA', 'ic_lens');

-- experience_step (48 filas)
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_explorar', 0, 'Un ser vivo entero', 'Vita es un ser vivo. Tiene partes distintas que trabajan en equipo.', NULL, 'ZOOM_CELULA', 'VITA_TRANQUILA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_explorar', 1, 'Toca y descubre', 'Toca una zona iluminada. Cada zona pertenece a un sistema.', NULL, 'SISTEMA_ILUMINA', 'LUPA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_explorar', 2, 'Nada esta suelto', 'Cada parte ayuda a las demas. Ninguna trabaja sola.', NULL, 'FLECHA_CONEXION', 'VITA_CURIOSA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_comer', 0, 'Llega el alimento', 'Arrastra una fruta hasta Vita. El alimento entra por la boca.', 'digestivo', 'ALIMENTO_VIAJA', 'MANZANA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_comer', 1, 'Se hace mas pequeno', 'Al masticar, el alimento se rompe en trozos muy pequenos.', 'digestivo', 'MASTICAR', 'BOCA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_comer', 2, 'Se mezcla por dentro', 'Dentro del cuerpo el alimento se mezcla y se transforma.', 'digestivo', 'MEZCLA_ESTOMAGO', 'ESTOMAGO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_comer', 3, 'Aparecen sustancias utiles', 'De esa mezcla salen sustancias utiles, como piezas pequenas de energia.', 'digestivo', 'LIBERAR_NUTRIENTES', 'NUTRIENTE');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_comer', 4, 'Empieza el reparto', 'Esas sustancias pasan a la sangre. Aqui entra otro sistema.', 'circulatorio', 'FLUJO_SANGRE', 'CAMINOS');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_respirar', 0, 'Entra el aire', 'Vita toma aire. El aire entra por la nariz.', 'respiratorio', 'AIRE_ENTRA', 'NARIZ');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_respirar', 1, 'Los pulmones se llenan', 'El aire llega a los pulmones, que se hacen mas grandes.', 'respiratorio', 'BURBUJA_OXIGENO', 'PULMONES');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_respirar', 2, 'El oxigeno pasa a la sangre', 'Del aire, el cuerpo aprovecha sobre todo el oxigeno.', 'circulatorio', 'BURBUJA_OXIGENO', 'BURBUJA_OXIGENO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_respirar', 3, 'Y sale el aire', 'Despues Vita suelta el aire que ya no necesita.', 'respiratorio', 'AIRE_SALE', 'VITA_RESPIRANDO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_moverse', 0, 'Vita quiere correr', 'Para moverse, Vita usa sus musculos.', 'movimiento', 'CARRERA', 'VITA_CORRIENDO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_moverse', 1, 'Los musculos piden ayuda', 'Los musculos necesitan energia y oxigeno para trabajar.', 'movimiento', 'BOMBEO_MUSCULO', 'MUSCULO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_moverse', 2, 'El corazon acelera', 'El corazon late mas rapido para repartir mas deprisa.', 'circulatorio', 'LATIDO', 'CORAZON');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_moverse', 3, 'Respiras mas rapido', 'Por eso, cuando corres, respiras mas veces por minuto.', 'respiratorio', 'AIRE_ENTRA', 'PULMONES');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_moverse', 4, 'Todo a la vez', 'Moverse no es cosa de un solo sistema. Son varios trabajando juntos.', NULL, 'SISTEMA_ILUMINA', 'VITA_CORRIENDO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_energia', 0, 'Empieza en el plato', 'Esta chispa era hace un momento un trozo de fruta.', 'digestivo', 'ALIMENTO_VIAJA', 'MANZANA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_energia', 1, 'Se transforma', 'El sistema digestivo la convierte en algo que el cuerpo puede usar.', 'digestivo', 'LIBERAR_NUTRIENTES', 'NUTRIENTE');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_energia', 2, 'Sube al transporte', 'La sangre la recoge y la lleva de viaje.', 'circulatorio', 'FLUJO_SANGRE', 'PARTICULA_ENERGIA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_energia', 3, 'Llega al musculo', 'Llega justo donde hace falta: al musculo que va a moverse.', 'movimiento', 'CHISPA_ENERGIA', 'MUSCULO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_energia', 4, 'Se convierte en movimiento', 'Esa energia se convierte en un salto de Vita.', 'movimiento', 'SALTO', 'VITA_CORRIENDO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_oxigeno', 0, 'Una burbuja en el aire', 'El oxigeno estaba en el aire, fuera del cuerpo.', 'respiratorio', 'AIRE_ENTRA', 'BURBUJA_OXIGENO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_oxigeno', 1, 'Entra con la respiracion', 'Vita respira y la burbuja entra hasta los pulmones.', 'respiratorio', 'BURBUJA_OXIGENO', 'PULMONES');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_oxigeno', 2, 'Cambia de vehiculo', 'En los pulmones, la burbuja pasa a la sangre.', 'circulatorio', 'FLUJO_SANGRE', 'CAMINOS');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_oxigeno', 3, 'Recorre el cuerpo', 'Viaja por caminos muy finos hasta cualquier rincon.', 'circulatorio', 'LATIDO', 'CORAZON');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_viaje_oxigeno', 4, 'Llega y ayuda', 'Junto con la energia, ayuda a que el cuerpo funcione.', 'movimiento', 'CHISPA_ENERGIA', 'MUSCULO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_conexiones', 0, 'Sistemas companeros', 'Cada sistema tiene companeros a los que ayuda.', NULL, 'FLECHA_CONEXION', 'CUADERNO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_conexiones', 1, 'Une con el dedo', 'Arrastra desde un sistema hasta otro para probar la conexion.', NULL, 'FLECHA_CONEXION', 'LUPA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_conexiones', 2, 'Cada union tiene explicacion', 'Si la union existe, te cuento por que funciona.', NULL, 'SISTEMA_ILUMINA', 'NORA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_secuencia', 0, 'Todo tiene un orden', 'En el cuerpo las cosas ocurren en un orden.', NULL, 'FLECHA_CONEXION', 'CUADERNO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_secuencia', 1, 'Coloca las tarjetas', 'Toca una tarjeta para colocarla en el siguiente hueco.', NULL, 'SISTEMA_ILUMINA', 'PROBETA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_juntos', 0, 'Vita come una fruta', 'Primero come. El sistema digestivo empieza a trabajar.', 'digestivo', 'MASTICAR', 'MANZANA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_juntos', 1, 'Se reparten las sustancias', 'La sangre reparte lo bueno del alimento por el cuerpo.', 'circulatorio', 'FLUJO_SANGRE', 'NUTRIENTE');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_juntos', 2, 'Vita empieza a correr', 'Los musculos se ponen en marcha y gastan energia.', 'movimiento', 'CARRERA', 'VITA_CORRIENDO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_juntos', 3, 'Respira mas rapido', 'Necesita mas oxigeno, asi que respira mas veces.', 'respiratorio', 'AIRE_ENTRA', 'PULMONES');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_juntos', 4, 'El corazon acompana', 'El corazon late mas fuerte para llegar a todas partes.', 'circulatorio', 'LATIDO', 'CORAZON');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_juntos', 5, 'Y se limpia lo que sobra', 'Al final, el cuerpo aparta lo que ya no le sirve.', 'limpieza', 'FILTRO_LIMPIEZA', 'FILTRO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_comparar', 0, 'Dos pruebas', 'Haremos la misma carrera dos veces, cambiando una cosa.', NULL, 'CARRERA', 'MATRAZ');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_comparar', 1, 'Observa la diferencia', 'Fijate en cuanto aguanta Vita en cada prueba.', NULL, 'CHISPA_ENERGIA', 'VITA_CORRIENDO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_limpieza', 0, 'No todo se aprovecha', 'De lo que entra, una parte no le sirve al cuerpo.', 'digestivo', 'LIBERAR_NUTRIENTES', 'DESECHO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_limpieza', 1, 'La sangre lo recoge', 'La sangre recoge esos restos mientras viaja.', 'circulatorio', 'FLUJO_SANGRE', 'CAMINOS');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_limpieza', 2, 'Pasa por los filtros', 'Unos filtros separan lo que sobra del resto.', 'limpieza', 'FILTRO_LIMPIEZA', 'FILTRO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_limpieza', 3, 'Sale del cuerpo', 'Al final sale del cuerpo, como cuando bebes mucha agua.', 'limpieza', 'GOTA_AGUA', 'GOTA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_sentidos', 0, 'Algo pasa fuera', 'Vita nota un ruido o una luz cerca.', 'relacion', 'ONDA_SENTIDO', 'VITA_CURIOSA');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_sentidos', 1, 'El aviso viaja', 'Ese aviso llega rapido al resto del cuerpo.', 'relacion', 'ONDA_SENTIDO', 'CAMINOS');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_sentidos', 2, 'El cuerpo responde', 'Vita se aparta o se acerca. El movimiento es la respuesta.', 'movimiento', 'SALTO', 'VITA_CORRIENDO');
INSERT INTO experience_step (experienceId, orderIndex, title, text, systemId, animationKey, illustrationKey) VALUES ('exp_sentidos', 3, 'Relacionarse tambien es vivir', 'Los seres vivos responden a lo que ocurre a su alrededor.', NULL, 'CELEBRACION', 'VITA_TRANQUILA');

-- interactive_element (30 filas)
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_boca', 'Boca', 'digestivo', 'Por aqui entra el alimento y empieza a hacerse pequenito.', 0.5, 0.235, 0.062, 'BOCA');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_tubo', 'Tubo del alimento', 'digestivo', 'Un tobogan blando que lleva el alimento hacia dentro.', 0.5, 0.325, 0.062, 'TUBO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_estomago', 'Bolsa mezcladora', 'digestivo', 'Aqui el alimento se mezcla hasta quedar como un pure.', 0.41, 0.44, 0.062, 'ESTOMAGO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_intestino', 'Camino largo', 'digestivo', 'Un camino muy largo donde se recogen las sustancias utiles.', 0.5, 0.6, 0.062, 'INTESTINO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_gusto', 'Sabor', 'digestivo', 'El sabor avisa a Vita de que esta comiendo.', 0.56, 0.265, 0.062, 'BOCA');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_nariz', 'Nariz', 'respiratorio', 'Por aqui entra el aire, limpio y calentito.', 0.575, 0.195, 0.062, 'NARIZ');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_via_aire', 'Tubo del aire', 'respiratorio', 'El aire baja por un tubo distinto al del alimento.', 0.5, 0.29, 0.062, 'TUBO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_pulmon_izq', 'Bolsa de aire izquierda', 'respiratorio', 'Se hincha cuando Vita toma aire.', 0.375, 0.365, 0.062, 'PULMONES');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_pulmon_der', 'Bolsa de aire derecha', 'respiratorio', 'Se deshincha cuando Vita suelta el aire.', 0.625, 0.365, 0.062, 'PULMONES');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_corazon', 'Bomba', 'circulatorio', 'Late sin parar y empuja la sangre por todo el cuerpo.', 0.5, 0.415, 0.062, 'CORAZON');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_camino_alto', 'Camino de subida', 'circulatorio', 'Lleva sustancias hacia la parte de arriba de Vita.', 0.295, 0.3, 0.062, 'CAMINOS');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_camino_bajo', 'Camino de bajada', 'circulatorio', 'Lleva sustancias hacia la parte de abajo.', 0.695, 0.545, 0.062, 'CAMINOS');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_red_fina', 'Caminos finos', 'circulatorio', 'Caminos tan finos como un hilo que llegan a todas partes.', 0.5, 0.715, 0.062, 'CAMINOS');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_vaso_pequeno', 'Curva del camino', 'circulatorio', 'La sangre da una vuelta completa una y otra vez.', 0.725, 0.3, 0.062, 'CAMINOS');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_brazo_izq', 'Musculo del brazo', 'movimiento', 'Se encoge y se estira para levantar cosas.', 0.235, 0.44, 0.062, 'MUSCULO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_brazo_der', 'Musculo del otro brazo', 'movimiento', 'Trabaja en pareja con el otro brazo.', 0.765, 0.44, 0.062, 'MUSCULO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_pierna_izq', 'Musculo de la pierna', 'movimiento', 'Empuja el suelo para que Vita avance.', 0.42, 0.815, 0.062, 'MUSCULO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_pierna_der', 'Musculo de la otra pierna', 'movimiento', 'Se turnan: uno empuja mientras el otro descansa.', 0.58, 0.815, 0.062, 'MUSCULO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_soporte', 'Soporte del cuerpo', 'movimiento', 'Una parte dura que sujeta a Vita, como el armazon de una tienda.', 0.5, 0.675, 0.062, 'MUSCULO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_articulacion', 'Bisagra', 'movimiento', 'Permite doblar el cuerpo, como la bisagra de una puerta.', 0.3, 0.715, 0.062, 'MUSCULO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_filtro_izq', 'Filtro izquierdo', 'limpieza', 'Separa lo que sobra de lo que aun sirve.', 0.375, 0.625, 0.062, 'FILTRO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_filtro_der', 'Filtro derecho', 'limpieza', 'Trabaja en pareja con el otro filtro.', 0.625, 0.625, 0.062, 'FILTRO');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_salida', 'Salida', 'limpieza', 'Por aqui sale lo que el cuerpo ya no necesita.', 0.5, 0.885, 0.062, 'GOTA');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_poros', 'Piel que suda', 'limpieza', 'Al sudar tambien sale agua y el cuerpo se refresca.', 0.285, 0.575, 0.062, 'GOTA');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_ojos', 'Ojos', 'relacion', 'Ven lo que ocurre cerca y avisan enseguida.', 0.445, 0.155, 0.062, 'VITA_CURIOSA');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_oido', 'Oido', 'relacion', 'Escucha los sonidos del laboratorio.', 0.345, 0.145, 0.062, 'VITA_CURIOSA');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_antena', 'Antena sensora', 'relacion', 'Nota los cambios del aire, como un bigote de gato.', 0.5, 0.075, 0.062, 'VITA_CURIOSA');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_piel', 'Piel', 'relacion', 'Nota el frio, el calor y las cosquillas.', 0.775, 0.6, 0.062, 'VITA_TRANQUILA');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_tacto', 'Punta sensible', 'relacion', 'Con ella Vita toca las cosas con cuidado.', 0.8, 0.72, 0.062, 'VITA_TRANQUILA');
INSERT INTO interactive_element (id, name, systemId, description, x, y, radius, illustrationKey) VALUES ('el_centro', 'Centro de avisos', 'relacion', 'Recibe los avisos y decide que hacer.', 0.5, 0.13, 0.062, 'VITA_CURIOSA');

-- activity (20 filas)
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_comer_arrastrar', 'exp_comer', 'ARRASTRAR', 'Dale de comer a Vita', 'Arrastra un alimento hasta Vita para empezar el recorrido.', 'Vita lleva toda la manana explorando y tiene hambre.', 1, 15);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_comer_orden', 'exp_comer', 'ORDENAR', 'Que ocurre primero?', 'Coloca las tarjetas en el orden correcto.', 'Las notas del cuaderno se han caido y se han mezclado.', 1, 15);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_comer_predecir', 'exp_comer', 'PREDECIR', 'Adonde va lo bueno del alimento?', 'Elige el sistema que recibira las sustancias utiles.', 'El alimento ya se ha transformado dentro de Vita.', 2, 15);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_respirar_observar', 'exp_respirar', 'OBSERVAR', 'Cuenta las respiraciones', 'Toca a Vita cada vez que toma aire.', 'Vita esta tumbada y tranquila en el laboratorio.', 1, 15);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_respirar_orden', 'exp_respirar', 'ORDENAR', 'El camino del aire', 'Ordena el recorrido que hace el aire.', 'Nora ha dibujado el camino del aire, pero sin numeros.', 2, 15);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_respirar_conectar', 'exp_respirar', 'CONECTAR', 'Quien recoge el oxigeno?', 'Une el sistema respiratorio con quien transporta el oxigeno.', 'La burbuja de oxigeno esta esperando en los pulmones.', 2, 20);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_mover_predecir', 'exp_moverse', 'PREDECIR', 'Que necesita un musculo?', 'Marca lo que hace falta para que un musculo trabaje.', 'Vita se prepara para saltar desde una piedra.', 2, 15);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_mover_orden', 'exp_moverse', 'ORDENAR', 'Antes de correr', 'Ordena lo que ocurre justo antes de que Vita corra.', 'Vita ve una mariposa y quiere seguirla.', 2, 15);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_mover_comparar', 'exp_moverse', 'COMPARAR', 'Caminar o correr?', 'Compara que pasa dentro de Vita en cada caso.', 'Vita hace el mismo camino andando y despues corriendo.', 2, 20);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_energia_arrastrar', 'exp_viaje_energia', 'ARRASTRAR', 'Lleva la chispa', 'Arrastra la chispa de energia por cada punto del recorrido.', 'La chispa acaba de salir del sistema digestivo.', 2, 20);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_energia_orden', 'exp_viaje_energia', 'ORDENAR', 'El viaje completo', 'Ordena el viaje de la energia desde la fruta hasta el salto.', 'Nora quiere pegar el recorrido en su cuaderno.', 3, 20);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_oxigeno_observar', 'exp_viaje_oxigeno', 'OBSERVAR', 'Sigue la burbuja', 'Toca la burbuja en cada parada de su viaje.', 'Una burbuja de oxigeno entra con la siguiente respiracion.', 2, 20);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_oxigeno_conectar', 'exp_viaje_oxigeno', 'CONECTAR', 'De donde a donde?', 'Une los sistemas que participan en el viaje del oxigeno.', 'El oxigeno ha cambiado de vehiculo dentro del cuerpo.', 3, 20);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_conexiones_libre', 'exp_conexiones', 'CONECTAR', 'Descubre las conexiones', 'Une parejas de sistemas y descubre por que se ayudan.', 'El panel de conexiones del laboratorio esta vacio.', 3, 25);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_conexiones_reto', 'exp_conexiones', 'CONECTAR', 'Reto de conexiones', 'Encuentra tres conexiones concretas.', 'Nora ha marcado tres huecos en el panel.', 3, 25);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_secuencia_gran', 'exp_secuencia', 'ORDENAR', 'De la fruta al salto', 'Ordena las seis tarjetas del gran recorrido.', 'Es el recorrido mas largo del cuaderno.', 3, 25);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_juntos_comparar', 'exp_juntos', 'COMPARAR', 'Con desayuno o sin desayuno?', 'Compara las dos carreras de Vita.', 'Vita corre por la pradera dos mananas distintas.', 3, 25);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_comparar_aire', 'exp_comparar', 'COMPARAR', 'Mucho aire o poco aire?', 'Compara la simulacion con mas y con menos oxigeno.', 'En el laboratorio se puede regular el aire de la camara.', 3, 25);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_limpieza_predecir', 'exp_limpieza', 'PREDECIR', 'Que hace el cuerpo con lo que sobra?', 'Elige que ocurre con los restos.', 'Vita ha bebido mucha agua despues de correr.', 2, 20);
INSERT INTO activity (id, experienceId, kind, title, prompt, situation, difficulty, xpReward) VALUES ('act_sentidos_comparar', 'exp_sentidos', 'COMPARAR', 'Nota o no nota?', 'Compara a Vita atenta y a Vita distraida.', 'Una hoja cae cerca de Vita mientras explora.', 2, 20);

-- sequence (5 filas)
INSERT INTO sequence (id, activityId, title, explanation) VALUES ('seq_comer', 'act_comer_orden', 'Cuando Vita come', 'El alimento entra, se transforma y despues se reparte por el cuerpo.');
INSERT INTO sequence (id, activityId, title, explanation) VALUES ('seq_aire', 'act_respirar_orden', 'El camino del aire', 'El aire entra, llega a los pulmones y el oxigeno pasa a la sangre.');
INSERT INTO sequence (id, activityId, title, explanation) VALUES ('seq_mover', 'act_mover_orden', 'Antes de correr', 'Primero llega la energia y el oxigeno, y despues los musculos trabajan.');
INSERT INTO sequence (id, activityId, title, explanation) VALUES ('seq_energia', 'act_energia_orden', 'El viaje de la energia', 'La energia sale del alimento, viaja en la sangre y termina en movimiento.');
INSERT INTO sequence (id, activityId, title, explanation) VALUES ('seq_gran', 'act_secuencia_gran', 'De la fruta al salto', 'Comer, digerir, transportar, respirar, obtener energia y moverse.');

-- sequence_item (23 filas)
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_comer', 'Vita muerde la fruta', 0, 'digestivo', 'MANZANA');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_comer', 'El alimento se mezcla', 1, 'digestivo', 'ESTOMAGO');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_comer', 'Aparecen sustancias utiles', 2, 'digestivo', 'NUTRIENTE');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_comer', 'La sangre las reparte', 3, 'circulatorio', 'CAMINOS');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_aire', 'Entra el aire por la nariz', 0, 'respiratorio', 'NARIZ');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_aire', 'Los pulmones se llenan', 1, 'respiratorio', 'PULMONES');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_aire', 'El oxigeno pasa a la sangre', 2, 'circulatorio', 'BURBUJA_OXIGENO');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_aire', 'Vita suelta el aire', 3, 'respiratorio', 'VITA_RESPIRANDO');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_mover', 'La sangre trae energia', 0, 'circulatorio', 'PARTICULA_ENERGIA');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_mover', 'Tambien llega oxigeno', 1, 'respiratorio', 'BURBUJA_OXIGENO');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_mover', 'El musculo se prepara', 2, 'movimiento', 'MUSCULO');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_mover', 'Vita corre', 3, 'movimiento', 'VITA_CORRIENDO');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_energia', 'Un trozo de fruta', 0, 'digestivo', 'MANZANA');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_energia', 'Se transforma dentro', 1, 'digestivo', 'NUTRIENTE');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_energia', 'Viaja en la sangre', 2, 'circulatorio', 'PARTICULA_ENERGIA');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_energia', 'Llega al musculo', 3, 'movimiento', 'MUSCULO');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_energia', 'Vita salta', 4, 'movimiento', 'VITA_CORRIENDO');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_gran', 'Vita come una fruta', 0, 'digestivo', 'MANZANA');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_gran', 'El alimento se transforma', 1, 'digestivo', 'ESTOMAGO');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_gran', 'La sangre transporta', 2, 'circulatorio', 'CAMINOS');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_gran', 'Vita respira mas fuerte', 3, 'respiratorio', 'PULMONES');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_gran', 'El musculo recibe energia', 4, 'movimiento', 'PARTICULA_ENERGIA');
INSERT INTO sequence_item (sequenceId, label, correctPosition, systemId, illustrationKey) VALUES ('seq_gran', 'Vita corre por la pradera', 5, 'movimiento', 'VITA_CORRIENDO');

-- connection_challenge (6 filas)
INSERT INTO connection_challenge (activityId, fromSystemId, toSystemId, explanation) VALUES ('act_respirar_conectar', 'respiratorio', 'circulatorio', 'El oxigeno pasa del aire a la sangre y empieza su viaje.');
INSERT INTO connection_challenge (activityId, fromSystemId, toSystemId, explanation) VALUES ('act_oxigeno_conectar', 'respiratorio', 'circulatorio', 'Primero el oxigeno cambia de vehiculo: del aire a la sangre.');
INSERT INTO connection_challenge (activityId, fromSystemId, toSystemId, explanation) VALUES ('act_oxigeno_conectar', 'circulatorio', 'movimiento', 'Despues la sangre lo entrega donde hace falta: en los musculos.');
INSERT INTO connection_challenge (activityId, fromSystemId, toSystemId, explanation) VALUES ('act_conexiones_reto', 'digestivo', 'circulatorio', 'Lo bueno del alimento pasa a la sangre.');
INSERT INTO connection_challenge (activityId, fromSystemId, toSystemId, explanation) VALUES ('act_conexiones_reto', 'circulatorio', 'limpieza', 'La sangre lleva hasta los filtros lo que ya no sirve.');
INSERT INTO connection_challenge (activityId, fromSystemId, toSystemId, explanation) VALUES ('act_conexiones_reto', 'movimiento', 'respiratorio', 'Al moverse mucho, el cuerpo pide mas aire.');

-- badge (12 filas)
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_primer', 'Primer Descubrimiento', 'Guardaste tu primera pegatina en el cuaderno.', 'ic_spark', 'PRIMER_DESCUBRIMIENTO', 1);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_aire', 'Explorador del Aire', 'Completaste el recorrido de la respiracion.', 'ic_lungs', 'RECORRIDOS', 2);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_energia', 'Viajero de la Energia', 'Seguiste la energia desde la fruta hasta el salto.', 'ic_spark', 'RECORRIDOS', 3);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_recorrido', 'Maestro del Recorrido', 'Terminaste cinco recorridos completos.', 'ic_path', 'RECORRIDOS', 5);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_conector', 'Conector de Sistemas', 'Descubriste cinco conexiones verdaderas.', 'ic_link', 'CONEXIONES', 5);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_observador', 'Gran Observador', 'Exploraste quince zonas del cuerpo de Vita.', 'ic_lens', 'ELEMENTOS', 15);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_movimiento', 'Explorador del Movimiento', 'Resolviste tres secuencias sobre el movimiento.', 'ic_muscle', 'SECUENCIAS', 3);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_curioso', 'Cientifico Curioso', 'Completaste ocho actividades del laboratorio.', 'ic_flask', 'ACTIVIDADES', 8);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_conexiones_todas', 'Descubridor de Conexiones', 'Encontraste las diez conexiones del panel.', 'ic_link', 'CONEXIONES', 10);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_aventurero', 'Aventurero de la Vida', 'Visitaste los seis sistemas de Vita.', 'ic_leaf', 'TODOS_LOS_SISTEMAS', 6);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_gran_explorador', 'Gran Explorador', 'Completaste ocho experiencias.', 'ic_badge', 'EXPERIENCIAS', 8);
INSERT INTO badge (id, name, description, iconKey, ruleKey, threshold) VALUES ('bdg_maestro', 'Maestro ExploraVida', 'Conseguiste todas las demas insignias.', 'ic_badge', 'MAESTRO', 0);

-- Perfil de ejemplo (la app lo crea al terminar el onboarding).
INSERT INTO user_profile (id, alias, avatarId, soundEnabled, hapticsEnabled, onboardingDone, createdAt)
VALUES (1, 'Explorador', 0, 1, 1, 1, 0);

INSERT INTO progress (profileId, xp, level, experiencesCompleted, activitiesCompleted, perfectActivities,
                      journeysCompleted, sequencesSolved, connectionsMade, elementsExplored,
                      discoveries, visitedSystems, updatedAt)
VALUES (1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, '', 0);

-- explorer_notebook (15 filas)
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_ser_vivo', 1, 'Ser vivo', 0, 'ic_leaf', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_sistema', 1, 'Sistema', 1, 'ic_link', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_alimento', 1, 'Alimento', 2, 'ic_leaf', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_digestion', 1, 'Digestion', 3, 'ic_flask', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_nutriente', 1, 'Sustancia util', 4, 'ic_spark', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_respiracion', 1, 'Respiracion', 5, 'ic_lungs', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_oxigeno', 1, 'Oxigeno', 6, 'ic_drop', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_circulacion', 1, 'Circulacion', 7, 'ic_heartbeat', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_energia', 1, 'Energia', 8, 'ic_spark', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_movimiento', 1, 'Movimiento', 9, 'ic_muscle', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_limpieza', 1, 'Limpieza del cuerpo', 10, 'ic_drop', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_relacion', 1, 'Relacion con el entorno', 11, 'ic_lens', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_conexion', 1, 'Conexion', 12, 'ic_link', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_cooperacion', 1, 'Cooperacion', 13, 'ic_badge', 0);
INSERT INTO explorer_notebook (id, profileId, title, pageIndex, stickerKey, unlocked) VALUES ('page_recorrido', 1, 'Recorrido', 14, 'ic_path', 0);
