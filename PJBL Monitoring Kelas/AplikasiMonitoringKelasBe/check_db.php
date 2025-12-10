<?php

require_once 'vendor/autoload.php';

$app = require_once 'bootstrap/app.php';
$app->make(Illuminate\Contracts\Console\Kernel::class)->bootstrap();

use Illuminate\Support\Facades\DB;

echo "Teachers:\n";
$teachers = DB::table('teachers')->get();
foreach ($teachers as $teacher) {
    echo $teacher->id . ': ' . $teacher->name . "\n";
}

echo "\nSchedules:\n";
$schedules = DB::table('schedules')->get();
foreach ($schedules as $schedule) {
    echo $schedule->id . ': guru_id=' . $schedule->guru_id . "\n";
}

echo "\nForeign keys:\n";
$fk = DB::select("SELECT CONSTRAINT_NAME, REFERENCED_TABLE_NAME FROM information_schema.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'schedules' AND COLUMN_NAME = 'guru_id'");
foreach ($fk as $f) {
    echo $f->CONSTRAINT_NAME . ' -> ' . $f->REFERENCED_TABLE_NAME . "\n";
}
