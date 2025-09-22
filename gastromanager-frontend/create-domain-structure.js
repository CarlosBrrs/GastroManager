const fs = require('fs');
const path = require('path');

const domainName = process.argv[2];
if (!domainName) {
  console.error('Debes indicar el nombre del dominio. Ejemplo: node create-domain-structure.js submenus');
  process.exit(1);
}

const base = path.join('src', 'app', 'features', domainName);

// Estructura de carpetas y subcarpetas
const structure = [
  { dir: '', file: `${domainName}.routes.ts` },
  { dir: 'domain/models' },
  { dir: 'domain/ports' },
  { dir: 'application/mappers' },
  { dir: 'application/usecases' },
  { dir: 'infrastructure/ui' },
  { dir: 'infrastructure/api' },
];

// Crear carpetas y archivo de rutas
structure.forEach(item => {
  const dirPath = path.join(base, item.dir);
  fs.mkdirSync(dirPath, { recursive: true });
  if (item.file) {
    const filePath = path.join(base, item.file);
    if (!fs.existsSync(filePath)) {
      fs.writeFileSync(filePath, `// Rutas para ${domainName}\n`);
    }
  }
});

console.log(`Estructura creada para el dominio "${domainName}"`);
