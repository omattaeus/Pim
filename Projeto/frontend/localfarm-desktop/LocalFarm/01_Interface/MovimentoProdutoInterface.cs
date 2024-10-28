﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace LocalFarm._01_Interface
{
    public partial class MovimentoProdutoInterface : Form
    {
        public MovimentoProdutoInterface()
        {
            InitializeComponent();
        }

        private void MovimentoProdutoInterface_Load(object sender, EventArgs e)
        {
            tabControle.Appearance = TabAppearance.FlatButtons;
            tabControle.ItemSize = new Size(0, 1);
            tabControle.SizeMode = TabSizeMode.Fixed;

            foreach (TabPage tab in tabControle.TabPages)
            {
                tab.Text = "";
            }
        }

        private void btnAdicionar_Click(object sender, EventArgs e)
        {
            tabControle.SelectedTab = tabManutencao;
        }

        private void btnSalvar_Click(object sender, EventArgs e)
        {
            tabControle.SelectedTab = tabGeral;
        }

        private void btnCancelar_Click(object sender, EventArgs e)
        {
            tabControle.SelectedTab = tabGeral;
        }

        private void brnEditar_Click(object sender, EventArgs e)
        {
            tabControle.SelectedTab = tabManutencao;
        }

        private void btnFechar_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.OK;
            this.Close();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void label7_Click(object sender, EventArgs e)
        {

        }

        private void label9_Click(object sender, EventArgs e)
        {

        }

        private void label11_Click(object sender, EventArgs e)
        {

        }
    }
}