-- Agregar nuevas columnas a la tabla bicicleta
ALTER TABLE bicicleta 
ADD COLUMN usos_recomendados VARCHAR(500),
ADD COLUMN usos_no_recomendados VARCHAR(500),
ADD COLUMN imagen_url VARCHAR(255),
ADD COLUMN descripcion_detallada VARCHAR(1000);

-- Insertar bicicletas de montaña
INSERT INTO bicicleta (codigo_bicicleta, nombre_modelo, marca, categoria, precio, fecha_registro, description, disponibilidad, usos_recomendados, usos_no_recomendados, imagen_url, descripcion_detallada) VALUES
('MTB001', 'Trek Marlin 5', 'Trek', 'Montaña', 18.00, NOW(), 'Bicicleta de montaña con cuadro de aluminio', 'DISPONIBLE', 'Senderos de montaña,Terrenos irregulares,Descensos moderados', 'Saltos extremos,Descensos técnicos,Uso prolongado en asfalto', '/img/BiciTREK.jpg', 'Bicicleta de montaña con cuadro de aluminio, suspensión delantera y 21 velocidades. Ideal para terrenos irregulares y senderos forestales.'),

('MTB002', 'Giant XTC SLR 2', 'Giant', 'Montaña', 20.00, NOW(), 'Bicicleta de montaña con cuadro ligero', 'DISPONIBLE', 'Rutas técnicas,Ascensos prolongados,Ciclistas experimentados', 'Principiantes,Uso urbano,Bikeparks', '/img/BiciGiant.jpg', 'Bicicleta de montaña con cuadro ligero de aluminio ALUXX, horquilla de suspensión con 100mm de recorrido y frenos de disco hidráulicos.');

-- Insertar bicicletas de ruta
INSERT INTO bicicleta (codigo_bicicleta, nombre_modelo, marca, categoria, precio, fecha_registro, description, disponibilidad, usos_recomendados, usos_no_recomendados, imagen_url, descripcion_detallada) VALUES
('RUT001', 'Bicicleta Specialized Allez Sport', 'Specialized', 'Ruta', 14.00, NOW(), 'Bicicleta de ruta con cuadro de aluminio', 'DISPONIBLE', 'Carreteras asfaltadas,Rutas largas,Entrenamiento', 'Terrenos irregulares,Caminos de grava,Uso urbano intensivo', '/img/BiciAllez.jpg', 'Bicicleta de ruta con cuadro de aluminio E5 Premium, horquilla de carbono FACT y transmisión Shimano Claris de 8 velocidades.'),

('RUT002', 'Cannondale CAAD13', 'Cannondale', 'Ruta', 16.00, NOW(), 'Bicicleta de competición', 'ALQUILADA', 'Competición,Ciclistas avanzados,Alto rendimiento', 'Principiantes,Paseos recreativos,Rutas sin asfaltar', '/img/BiciCannondale.jpeg', 'Bicicleta de competición con cuadro de aluminio SmartForm C1, horquilla full carbon y grupo Shimano 105 de 11 velocidades.');

-- Insertar bicicletas urbanas
INSERT INTO bicicleta (codigo_bicicleta, nombre_modelo, marca, categoria, precio, fecha_registro, description, disponibilidad, usos_recomendados, usos_no_recomendados, imagen_url, descripcion_detallada) VALUES
('URB001', 'Bicicleta Electra Townie Path Go! 10D', 'Electra', 'Urbana', 10.00, NOW(), 'Bicicleta urbana con geometría especial', 'DISPONIBLE', 'Paseos por la ciudad,Recorridos cortos,Usuarios ocasionales', 'Rutas largas,Terrenos irregulares,Alta velocidad', '/img/BiciTownie.jpg', 'Bicicleta urbana con geometría Flat Foot Technology® para mayor comodidad, 7 velocidades y posición de pedaleo ergonómica.'),

('URB002', 'Bicicleta Schwinn Surbuban Deluxe', 'Schwinn', 'Urbana', 8.50, NOW(), 'Bicicleta híbrida', 'DISPONIBLE', 'Uso diario,Ciclovías,Commuting', 'Senderos técnicos,Competición,Descensos', '/img/BiciSchwinn.jpg', 'Bicicleta híbrida con cuadro de aluminio, suspensión delantera, 21 velocidades y guardabarros incluidos.');

-- Insertar bicicletas infantiles
INSERT INTO bicicleta (codigo_bicicleta, nombre_modelo, marca, categoria, precio, fecha_registro, description, disponibilidad, usos_recomendados, usos_no_recomendados, imagen_url, descripcion_detallada) VALUES
('INF001', 'Bicicleta Woom 3', 'Woom', 'Infantil', 13.00, NOW(), 'Bicicleta infantil para niños de 4-6 años', 'DISPONIBLE', 'Niños 4-6 años,Aprendizaje,Paseos familiares', 'Terrenos técnicos,Niños mayores de 6 años,Saltos', '/img/BiciWoom.jpg', 'Bicicleta infantil para niños de 4-6 años, cuadro ultraligero de aluminio y frenos de fácil accionamiento.'),

('INF002', 'Orbea MX 20 XC', 'Orbea', 'Infantil', 7.50, NOW(), 'Bicicleta para niños de 7-10 años', 'FUERA_DE_SERVICIO', 'Niños 7-10 años,Primeros paseos en terreno variado,Uso recreativo', 'Niños menores de 7 años,Descensos técnicos,Uso competitivo', '/img/BiciOrbea.jpg', 'Bicicleta para niños de 7-10 años con cuadro de aluminio, 7 velocidades y frenos potentes pero seguros.');

-- Insertar bicicletas BMX
INSERT INTO bicicleta (codigo_bicicleta, nombre_modelo, marca, categoria, precio, fecha_registro, description, disponibilidad, usos_recomendados, usos_no_recomendados, imagen_url, descripcion_detallada) VALUES
('BMX001', 'Bicicleta BMX We The People Crysis', 'We The People', 'BMX', 22.00, NOW(), 'Bicicleta BMX profesional', 'DISPONIBLE', 'Riders avanzados,Street riding,Competición', 'Principiantes,Desplazamientos largos,Paseos recreativos', '/img/BiciBMX.jpg', 'Bicicleta BMX profesional con cuadro de cromo-molibdeno 4130, horquilla aftermarket y componentes premium para trucos avanzados y street riding.'),

('BMX002', 'Bicicleta BMX Kink Gap', 'Kink', 'BMX', 24.00, NOW(), 'BMX versátil', 'DISPONIBLE', 'Skatepark,Rampas,Riders intermedios', 'Uso urbano prolongado,Rutas largas,Terrenos de montaña', '/img/BiciBmx.webp', 'BMX versátil con cuadro de cromo-molibdeno completo, buje cassette sellado y llantas de doble pared para mayor durabilidad en trucos y saltos.');