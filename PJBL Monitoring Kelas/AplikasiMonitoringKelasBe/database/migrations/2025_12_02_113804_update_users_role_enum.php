<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::table('users', function (Blueprint $table) {
            // Update the enum to include all roles used in the application
            DB::statement("ALTER TABLE users MODIFY role ENUM('admin', 'guru', 'siswa', 'kurikulum', 'kepala_sekolah') NOT NULL DEFAULT 'siswa'");
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('users', function (Blueprint $table) {
            // Revert to previous enum values
            DB::statement("ALTER TABLE users MODIFY role ENUM('admin', 'siswa', 'kurikulum') NOT NULL DEFAULT 'siswa'");
        });
    }
};
