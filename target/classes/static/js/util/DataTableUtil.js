$.extend($.fn.dataTable.defaults, {
    destroy: true,
    paging: true,
    searching: false,
    ordering: true,
    scrollX: true,
    scrollY: true,
    pagingType: "full_numbers",
    serverSide: true,
    processing: true,
    stateSave: true,
    order: [[1, "asc"]],
    /*dom: 'lBfrtip',
    buttons: [
        {
            extend: 'csv',
            className: 'btn btn-primary',
            text: '<i class="fas fa-file-csv"></i> CSV',
            exportOptions: {
                columns: ':visible'
            }
        },
        {
            extend: 'pdf',
            text: '<i class="fas fa-file-pdf"></i> PDF',
            exportOptions: {
                columns: ':visible'
            }
        },
        {
            extend: 'print',
            text: '<i class="fas fa-print"></i> Print',
            exportOptions: {
                columns: ':visible'
            }
        },
        {
            extend: 'colvis',
            text: '<i class="fas fa-eye"></i> Column Visibility'
        }
    ],*/
    fixedHeader: true
});