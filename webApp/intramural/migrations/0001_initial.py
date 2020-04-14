# Generated by Django 3.0.4 on 2020-04-14 16:04

import django.core.validators
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Sport',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(default='', max_length=50, unique=True)),
                ('max_teams_capacity', models.IntegerField(default=0, validators=[django.core.validators.MaxValueValidator(12)])),
                ('max_players_capacity', models.IntegerField(default=0, validators=[django.core.validators.MaxValueValidator(12)])),
            ],
        ),
        migrations.CreateModel(
            name='Users',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('first_name', models.CharField(default='', max_length=50)),
                ('last_name', models.CharField(default='', max_length=50)),
                ('email', models.CharField(default='', max_length=50, unique=True)),
                ('password', models.CharField(default='', max_length=50)),
                ('role', models.CharField(default='Student', max_length=7)),
            ],
        ),
        migrations.CreateModel(
            name='Venue',
            fields=[
                ('court', models.IntegerField(primary_key=True, serialize=False)),
            ],
        ),
        migrations.CreateModel(
            name='User_medical_info',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('birth_date', models.DateField()),
                ('height', models.IntegerField(default=0, validators=[django.core.validators.MaxValueValidator(300)])),
                ('weight', models.IntegerField(default=0, validators=[django.core.validators.MaxValueValidator(600)])),
                ('description', models.TextField(default='', max_length=200)),
                ('emerg_cont_name', models.TextField(default='', max_length=50)),
                ('emerg_cont_number', models.CharField(default='', max_length=10)),
                ('student_id', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='intramural.Users')),
            ],
        ),
        migrations.CreateModel(
            name='Teams',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(default='', max_length=50)),
                ('description', models.TextField(default='', max_length=200)),
                ('accepted', models.CharField(default='N', max_length=1)),
                ('sport', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='intramural.Sport')),
            ],
            options={
                'unique_together': {('name', 'sport')},
            },
        ),
        migrations.CreateModel(
            name='Schedule',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateField(null=True)),
                ('time', models.TimeField(null=True)),
                ('court', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='intramural.Venue')),
                ('sport', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='intramural.Sport')),
                ('team1', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='+', to='intramural.Teams')),
                ('team2', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='+', to='intramural.Teams')),
            ],
            options={
                'unique_together': {('sport', 'team1', 'team2', 'date', 'time', 'court')},
            },
        ),
    ]
