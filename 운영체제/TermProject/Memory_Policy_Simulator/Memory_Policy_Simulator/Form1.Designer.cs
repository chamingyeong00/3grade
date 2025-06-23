namespace Memory_Policy_Simulator
{
    partial class Form1
    {
        /// <summary>
        /// 필수 디자이너 변수입니다.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 사용 중인 모든 리소스를 정리합니다.
        /// </summary>
        /// <param name="disposing">관리되는 리소스를 삭제해야 하면 true이고, 그렇지 않으면 false입니다.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form 디자이너에서 생성한 코드

        /// <summary>
        /// 디자이너 지원에 필요한 메서드입니다. 
        /// 이 메서드의 내용을 코드 편집기로 수정하지 마세요.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.Windows.Forms.DataVisualization.Charting.ChartArea chartArea2 = new System.Windows.Forms.DataVisualization.Charting.ChartArea();
            System.Windows.Forms.DataVisualization.Charting.Legend legend2 = new System.Windows.Forms.DataVisualization.Charting.Legend();
            System.Windows.Forms.DataVisualization.Charting.Series series2 = new System.Windows.Forms.DataVisualization.Charting.Series();
            this.btnOperate = new System.Windows.Forms.Button();
            this.tbWindowSize = new System.Windows.Forms.TextBox();
            this.tbQueryString = new System.Windows.Forms.TextBox();
            this.pImage = new System.Windows.Forms.Panel();
            this.toolTip1 = new System.Windows.Forms.ToolTip(this.components);
            this.tbConsole = new System.Windows.Forms.TextBox();
            this.chart1 = new System.Windows.Forms.DataVisualization.Charting.Chart();
            this.btnRand = new System.Windows.Forms.Button();
            this.btnSave = new System.Windows.Forms.Button();
            this.comboBox1 = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.lbPageFaultRatio = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.chart1)).BeginInit();
            this.SuspendLayout();
            // 
            // btnOperate
            // 
            this.btnOperate.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.btnOperate.Location = new System.Drawing.Point(1031, 7);
            this.btnOperate.Name = "btnOperate";
            this.btnOperate.Size = new System.Drawing.Size(142, 70);
            this.btnOperate.TabIndex = 0;
            this.btnOperate.Text = "Run";
            this.btnOperate.UseVisualStyleBackColor = true;
            this.btnOperate.Click += new System.EventHandler(this.btnOperate_Click);
            // 
            // tbWindowSize
            // 
            this.tbWindowSize.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.tbWindowSize.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.tbWindowSize.Location = new System.Drawing.Point(766, 33);
            this.tbWindowSize.Name = "tbWindowSize";
            this.tbWindowSize.Size = new System.Drawing.Size(117, 34);
            this.tbWindowSize.TabIndex = 1;
            this.tbWindowSize.Text = "4";
            this.tbWindowSize.KeyDown += new System.Windows.Forms.KeyEventHandler(this.tbWindowSize_KeyDown);
            this.tbWindowSize.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.tbWindowSize_KeyPress);
            // 
            // tbQueryString
            // 
            this.tbQueryString.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.tbQueryString.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.tbQueryString.Location = new System.Drawing.Point(174, 33);
            this.tbQueryString.Name = "tbQueryString";
            this.tbQueryString.Size = new System.Drawing.Size(586, 34);
            this.tbQueryString.TabIndex = 2;
            // 
            // pImage
            // 
            this.pImage.AutoScroll = true;
            this.pImage.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.pImage.Location = new System.Drawing.Point(12, 97);
            this.pImage.Name = "pImage";
            this.pImage.Size = new System.Drawing.Size(871, 629);
            this.pImage.TabIndex = 4;
            // 
            // tbConsole
            // 
            this.tbConsole.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.tbConsole.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.tbConsole.Location = new System.Drawing.Point(889, 97);
            this.tbConsole.Multiline = true;
            this.tbConsole.Name = "tbConsole";
            this.tbConsole.ReadOnly = true;
            this.tbConsole.Size = new System.Drawing.Size(393, 203);
            this.tbConsole.TabIndex = 5;
            // 
            // chart1
            // 
            chartArea2.AlignmentOrientation = ((System.Windows.Forms.DataVisualization.Charting.AreaAlignmentOrientations)((System.Windows.Forms.DataVisualization.Charting.AreaAlignmentOrientations.Vertical | System.Windows.Forms.DataVisualization.Charting.AreaAlignmentOrientations.Horizontal)));
            chartArea2.Area3DStyle.LightStyle = System.Windows.Forms.DataVisualization.Charting.LightStyle.Realistic;
            chartArea2.Name = "ChartArea1";
            this.chart1.ChartAreas.Add(chartArea2);
            legend2.Docking = System.Windows.Forms.DataVisualization.Charting.Docking.Bottom;
            legend2.Name = "Legend1";
            this.chart1.Legends.Add(legend2);
            this.chart1.Location = new System.Drawing.Point(889, 306);
            this.chart1.Name = "chart1";
            this.chart1.Palette = System.Windows.Forms.DataVisualization.Charting.ChartColorPalette.Light;
            series2.ChartArea = "ChartArea1";
            series2.Legend = "Legend1";
            series2.Name = "Series1";
            this.chart1.Series.Add(series2);
            this.chart1.Size = new System.Drawing.Size(393, 390);
            this.chart1.TabIndex = 6;
            this.chart1.Text = "chart1";
            this.chart1.Click += new System.EventHandler(this.chart1_Click);
            // 
            // btnRand
            // 
            this.btnRand.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.btnRand.Location = new System.Drawing.Point(889, 6);
            this.btnRand.Name = "btnRand";
            this.btnRand.Size = new System.Drawing.Size(136, 71);
            this.btnRand.TabIndex = 7;
            this.btnRand.Text = "Random";
            this.btnRand.UseVisualStyleBackColor = true;
            this.btnRand.Click += new System.EventHandler(this.btnRand_Click);
            // 
            // btnSave
            // 
            this.btnSave.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.btnSave.Location = new System.Drawing.Point(1179, 6);
            this.btnSave.Name = "btnSave";
            this.btnSave.Size = new System.Drawing.Size(108, 71);
            this.btnSave.TabIndex = 7;
            this.btnSave.Text = "Save";
            this.btnSave.UseVisualStyleBackColor = true;
            this.btnSave.Click += new System.EventHandler(this.btnSave_Click);
            // 
            // comboBox1
            // 
            this.comboBox1.FormattingEnabled = true;
            this.comboBox1.Items.AddRange(new object[] {
            "FIFO"});
            this.comboBox1.Location = new System.Drawing.Point(12, 34);
            this.comboBox1.Name = "comboBox1";
            this.comboBox1.Size = new System.Drawing.Size(121, 32);
            this.comboBox1.TabIndex = 8;
            this.comboBox1.Text = "FIFO";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.label1.Location = new System.Drawing.Point(39, 2);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(67, 27);
            this.label1.TabIndex = 9;
            this.label1.Text = "Policy";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.label2.Location = new System.Drawing.Point(377, 2);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(174, 27);
            this.label2.TabIndex = 9;
            this.label2.Text = "Reference String";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.label3.Location = new System.Drawing.Point(771, 1);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(89, 27);
            this.label3.TabIndex = 9;
            this.label3.Text = "#Frame";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.label4.Location = new System.Drawing.Point(889, 699);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(240, 27);
            this.label4.TabIndex = 9;
            this.label4.Text = "Page Fault Rate (%) = ";
            // 
            // lbPageFaultRatio
            // 
            this.lbPageFaultRatio.AutoSize = true;
            this.lbPageFaultRatio.Font = new System.Drawing.Font("Tahoma", 8.25F);
            this.lbPageFaultRatio.Location = new System.Drawing.Point(1121, 699);
            this.lbPageFaultRatio.Name = "lbPageFaultRatio";
            this.lbPageFaultRatio.Size = new System.Drawing.Size(0, 27);
            this.lbPageFaultRatio.TabIndex = 9;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(13F, 24F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1299, 739);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.lbPageFaultRatio);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.comboBox1);
            this.Controls.Add(this.btnSave);
            this.Controls.Add(this.btnRand);
            this.Controls.Add(this.chart1);
            this.Controls.Add(this.tbConsole);
            this.Controls.Add(this.pImage);
            this.Controls.Add(this.tbQueryString);
            this.Controls.Add(this.tbWindowSize);
            this.Controls.Add(this.btnOperate);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "Form1";
            this.ShowIcon = false;
            this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
            this.Text = "Memory Simulator";
            ((System.ComponentModel.ISupportInitialize)(this.chart1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btnOperate;
        private System.Windows.Forms.TextBox tbWindowSize;
        private System.Windows.Forms.TextBox tbQueryString;
        private System.Windows.Forms.Panel pImage;
        private System.Windows.Forms.ToolTip toolTip1;
        private System.Windows.Forms.TextBox tbConsole;
        private System.Windows.Forms.DataVisualization.Charting.Chart chart1;
        private System.Windows.Forms.Button btnRand;
        private System.Windows.Forms.Button btnSave;
        private System.Windows.Forms.ComboBox comboBox1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label lbPageFaultRatio;
    }
}

