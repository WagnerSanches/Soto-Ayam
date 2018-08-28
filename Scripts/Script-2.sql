SELECT Energi_Java.Simbol,Energi_EJ/(Energi_EJ+Energi_Ex+Energi_T+Energi_V),Energi_Ex/(Energi_EJ+Energi_Ex+Energi_T+Energi_V)
,Energi_T/(Energi_EJ+Energi_Ex+Energi_T+Energi_V),Energi_V/(Energi_EJ+Energi_Ex+Energi_T+Energi_V),xyz_molecule.E_ref
from Quantum.Energi_Java,Quantum.xyz_molecule where Energi_Java.ID like "%hf_6-31%" and Energi_Java.Simbol=xyz_molecule.Simbol;