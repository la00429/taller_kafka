// Crear base de datos y colecciones
db = db.getSiblingDB('cqrs_queries');

// Crear colecciones
db.createCollection('customers');
db.createCollection('logins');
db.createCollection('orders');

print('MongoDB inicializado correctamente con autenticación');
