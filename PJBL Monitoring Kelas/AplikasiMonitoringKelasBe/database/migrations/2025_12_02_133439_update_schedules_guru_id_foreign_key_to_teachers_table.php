<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Support\Facades\DB;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        // Get the current foreign key constraint name
        $fkResult = DB::select("SELECT CONSTRAINT_NAME
            FROM information_schema.KEY_COLUMN_USAGE
            WHERE TABLE_SCHEMA = DATABASE()
            AND TABLE_NAME = 'schedules'
            AND COLUMN_NAME = 'guru_id'
            AND REFERENCED_TABLE_NAME = 'users'");

        if (!empty($fkResult)) {
            $fkName = $fkResult[0]->CONSTRAINT_NAME;

            // Temporarily disable foreign key checks
            DB::statement('SET FOREIGN_KEY_CHECKS=0;');

            // Drop the existing foreign key
            DB::statement("ALTER TABLE schedules DROP FOREIGN KEY `{$fkName}`");

            // Add new foreign key to teachers table
            DB::statement("ALTER TABLE schedules
                ADD CONSTRAINT schedules_guru_id_foreign
                FOREIGN KEY (guru_id) REFERENCES teachers(id) ON DELETE CASCADE");

            DB::statement('SET FOREIGN_KEY_CHECKS=1;');
        }
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        // Get the current foreign key constraint name
        $fkResult = DB::select("SELECT CONSTRAINT_NAME
            FROM information_schema.KEY_COLUMN_USAGE
            WHERE TABLE_SCHEMA = DATABASE()
            AND TABLE_NAME = 'schedules'
            AND COLUMN_NAME = 'guru_id'
            AND REFERENCED_TABLE_NAME = 'teachers'");

        if (!empty($fkResult)) {
            $fkName = $fkResult[0]->CONSTRAINT_NAME;

            // Temporarily disable foreign key checks
            DB::statement('SET FOREIGN_KEY_CHECKS=0;');

            // Drop the existing foreign key
            DB::statement("ALTER TABLE schedules DROP FOREIGN KEY `{$fkName}`");

            // Add back foreign key to users table
            DB::statement("ALTER TABLE schedules
                ADD CONSTRAINT schedules_guru_id_foreign
                FOREIGN KEY (guru_id) REFERENCES users(id) ON DELETE CASCADE");

            DB::statement('SET FOREIGN_KEY_CHECKS=1;');
        }
    }
};
